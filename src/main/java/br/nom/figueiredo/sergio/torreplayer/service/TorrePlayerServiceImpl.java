package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.exception.MusicaException;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class TorrePlayerServiceImpl implements TorrePlayerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TorrePlayerServiceImpl.class);
    private final ConfigService configService;

    @Value("${torre.cmd.play}")
    private String cmdPlayTorre;

    @Value("${torre.cmd.spaceEscape}")
    private String cmdSpaceScape;

    @Value("${torre.cmd.stop.mode}")
    private TorrePlayerStopMode stopMode;

    /*
     * Teclas a serem enviadas para o processo para terminá-lo.
     */
    @Value("${torre.cmd.stop.keys}")
    private String stopKeys;

    @Value("${torre.cmd.volume.remap.min}")
    private Integer volumeRemapMin;

    @Value("${torre.cmd.volume.remap.max}")
    private Integer volumeRemapMax;

    // charset válidos: https://docs.oracle.com/javase/8/docs/api/java/nio/charset/Charset.html
    @Value("${torre.cmd.stdout.charset}")
    private String charset;

    private Process process;
    private TorrePlayerInfo info;

    @Autowired
    public TorrePlayerServiceImpl(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public synchronized void tocar(Musica musica) {

        if (this.isTocando()) {
            throw new MusicaException("Já tem música tocando...");
        }
        this.info = new TorrePlayerInfo();
        this.info.setMusica(musica);
        this.info.setStatus(TorrePlayerStatus.INICIANDO);
        // triggers the async task, which updates the cn status accordingly
        ProcessBuilder processBuilder = new ProcessBuilder();

        String absolutePath = musica.getAbsolutePath().replace(" ", this.cmdSpaceScape);

        Integer volumeToDevice;
        if (nonNull(volumeRemapMin) && nonNull(volumeRemapMax) && (volumeRemapMin < volumeRemapMax)) {
            volumeToDevice = remap(configService.getConfiguracoes().getMasterVolume(),
                    configService.getConfiguracoes().getMasterVolumeMin(),
                    configService.getConfiguracoes().getMasterVolumeMax(),
                    volumeRemapMin, volumeRemapMax);
        } else {
            volumeToDevice = configService.getConfiguracoes().getMasterVolume();
        }

        String cmd = cmdPlayTorre
                .replace("#musicaPath", absolutePath)
                .replace("#musicaVolume", Integer.toString(volumeToDevice));

        processBuilder.command(cmd.split(","));

        try {
            String cmdInfo = String.format("Executing command: %s", cmd.replace(',', ' '));
            LOGGER.info(cmdInfo);
            processBuilder.redirectErrorStream(true);
            this.process = processBuilder.start();
        } catch (IOException e) {
            throw new MusicaException(String.format("Error writing file to output stream. Musica was '%s'",
                    musica.getAbsolutePath()), e);
        }
    }

    private static Integer remap(Integer inputValue, Integer inputMin, Integer inputMax,
                                 Integer outpuMin, Integer outputMax) {
        if (inputValue == null) {
            throw new IllegalArgumentException("Input value cannot be null");
        }
        return (int) Math.round(((double) (inputValue - inputMin)) / (inputMax - inputMin) * (outputMax - outpuMin) + outpuMin);
    }

    /**
     * Indica que a música ainda está tocando.
     *
     * @return true se a música está tocando.
     */
    private synchronized boolean isTocando() {
        return nonNull(process) && process.isAlive();
    }

    @Override
    public synchronized void stop() {
        if (this.isTocando()) {
            LOGGER.debug("Parando a música...");
            if (nonNull(this.info)) {
                this.info.appendOutput("Parando a música...");
                this.info.setStatus(TorrePlayerStatus.PARANDO);
            }
            if (stopMode == TorrePlayerStopMode.KILL_PROCESS) {
                killProcess();
            } else if (stopMode == TorrePlayerStopMode.SEND_KEYS) {
                sendKeys(this.stopKeys);
            }
        }
    }

    private void sendKeys(String keys) {
        try {
            LOGGER.debug("Enviando teclas '{}'", keys);
            OutputStream out = this.process.getOutputStream();
            out.write(keys.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            if (nonNull(this.info)) {
                this.info.appendOutput("%nEnvio de teclas falhou");
            }
        }
    }

    private void killProcess() {
        LOGGER.debug("Matando o processo");
        this.process.destroy();
    }

    @Override
    public synchronized TorrePlayerInfo getInfo() {
        TorrePlayerInfo result;
        if (isNull(this.info)) {
            result = new TorrePlayerInfo();
        } else {
            result = this.info;
        }
        if (nonNull(process)) {
            try {
                byte[] bytes = new byte[process.getInputStream().available()];
                int lidos = process.getInputStream().read(bytes);
                if (lidos > 0) {
                    result.appendOutput(new String(bytes, this.charset));
                }
            } catch (IOException ex) {
                LOGGER.debug("Falha ao ler InputStream do player: {}", ex.getMessage());
            }
            if (process.isAlive()) {
                result.setStatus(TorrePlayerStatus.TOCANDO);
            } else {
                if (isNull(result.getExitValue())) {
                    result.setExitValue(process.exitValue());
                    if (this.process.exitValue() != 0) {
                        result.setStatus(TorrePlayerStatus.ENCERRADO_ERRO);
                        result.appendOutput(String.format("%nCódigo de encerramento: %d", this.process.exitValue()));
                    } else {
                        result.setStatus(TorrePlayerStatus.ENCERRADO_SUCESSO);
                        result.appendOutput("Música tocou com sucesso.");
                    }
                }
                this.process = null;
            }
        }

        return result;
    }
}
