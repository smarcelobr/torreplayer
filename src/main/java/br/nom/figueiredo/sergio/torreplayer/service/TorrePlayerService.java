package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.exception.MusicaException;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import org.apache.commons.io.IOUtils;
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
public class TorrePlayerService {

    private final Logger logger = LoggerFactory.getLogger(TorrePlayerService.class);
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

    private Process process;
    private TorrePlayerInfo info;

    @Autowired
    public TorrePlayerService(ConfigService configService) {
        this.configService = configService;
    }

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
        String cmd = cmdPlayTorre
                .replace("#musicaPath", absolutePath)
                .replace("#musicaVolume", Integer.toString(configService.getConfiguracoes().getMasterVolume()));
        processBuilder.command(cmd.split(","));

        try {
            String cmdInfo = String.format("Executing command: %s", cmd.replace(',', ' '));
            logger.info(cmdInfo);
            processBuilder.redirectErrorStream(true);
            this.process = processBuilder.start();
        } catch (IOException e) {
            throw new MusicaException(String.format("Error writing file to output stream. Musica was '%s'",
                    musica.getAbsolutePath()), e);
        }
    }

    /**
     * Indica que a música ainda está tocando.
     *
     * @return true se a música está tocando.
     */
    public synchronized boolean isTocando() {
        return nonNull(process) && process.isAlive();
    }

    public synchronized void stop() {
        if (this.isTocando()) {
            logger.debug("Parando a música...");
            if (stopMode == TorrePlayerStopMode.KILL_PROCESS) {
                killProcess();
            } else if (stopMode == TorrePlayerStopMode.SEND_KEYS) {
                sendKeys(this.stopKeys);
            }

            if (nonNull(this.info)) {
                this.info.setStatus(TorrePlayerStatus.PARANDO);
            }
        }
    }

    private void sendKeys(String keys) {
        try {
            logger.debug("Enviando teclas '{}'", keys);
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
        logger.debug("Matando o processo");
        this.process.destroy();
    }

    public synchronized TorrePlayerInfo getInfo() {
        TorrePlayerInfo result;
        if (isNull(this.info)) {
            result = new TorrePlayerInfo();
        } else {
            result = this.info;
        }
            if (nonNull(process)) {
                if (process.isAlive()) {
                    result.setStatus(TorrePlayerStatus.TOCANDO);
                } else {
                    if (isNull(result.getExitValue())) {
                        result.setExitValue(process.exitValue());
                        try {
                            result.appendOutput(IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8));
                        } catch (IOException ex) {
                            logger.debug("Falha ao ler InputStream do player", ex);
                            result.appendOutput("Não foi possível ler inputstream do do player.\n");
                        }
                        if (this.process.exitValue() != 0) {
                            result.setStatus(TorrePlayerStatus.ENCERRADO_ERRO);
                            result.appendOutput(String.format("%nExited with error code : %d", this.process.exitValue()));
                        } else {
                            result.setStatus(TorrePlayerStatus.ENCERRADO_SUCESSO);
                            result.appendOutput("Música tocou com sucesso.");
                        }
                    }
                }
            }

        return result;
    }
}
