package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.event.PlayerCommandCompletedEvent;
import br.nom.figueiredo.sergio.torreplayer.event.PlayerCommandStartedEvent;
import br.nom.figueiredo.sergio.torreplayer.event.PlayerCommandStopRequestedEvent;
import br.nom.figueiredo.sergio.torreplayer.exception.MusicaException;
import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Configuracoes;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.model.Playlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class PlayerCommandServiceImpl implements PlayerCommandService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PlayerCommandServiceImpl.class);
    private final ConfigService configService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${torre.cmd.play}")
    private String cmdPlayTorre;

    @Value("${torre.cmd.opt.repeat}")
    private String cmdPlayOptRepeat;

    @Value("${torre.cmd.opt.random}")
    private String cmdPlayOptRandom;

    @Value("${torre.cmd.taskkill}")
    private String cmdPlayTaskkill;

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

    public PlayerCommandServiceImpl(ConfigService configService, ApplicationEventPublisher applicationEventPublisher) {
        this.configService = configService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public synchronized void tocar(Musica musica, boolean repeat) {

        if (this.isTocando()) {
            throw new MusicaException("Som está ocupado tocando...");
        }
        LOGGER.info("Tocando musica: {}", musica.getNome());
        this.info = new TorrePlayerInfo();
        this.info.setArquivo(musica);
        this.info.setStatus(TorrePlayerStatus.INICIANDO);
        executaComando(musica.getAbsolutePath(), repeat, false);
    }

    @Override
    public synchronized void tocar(Album album, boolean repeat, boolean random) {

        if (this.isTocando()) {
            throw new MusicaException("Som está ocupado tocando...");
        }
        LOGGER.info("Tocando album: {}", album.getNome());
        this.info = new TorrePlayerInfo();
        this.info.setArquivo(album);
        this.info.setStatus(TorrePlayerStatus.INICIANDO);
        executaComando(album.getAbsolutePath(), repeat, random);
    }

    @Override
    public synchronized void tocar(Playlist playlist, boolean repeat, boolean random) {
        if (this.isTocando()) {
            throw new MusicaException("Som está ocupado tocando...");
        }
        LOGGER.info("Tocando playlist: {}", playlist.getNome());
        this.info = new TorrePlayerInfo();
        this.info.setArquivo(playlist);
        this.info.setStatus(TorrePlayerStatus.INICIANDO);
        executaComando(playlist.getAbsolutePath(), repeat, random);

    }

    private void executaComando(String absolutePath, boolean repeat, boolean random) {
        // triggers the async task, which updates the cn status accordingly
        ProcessBuilder processBuilder = new ProcessBuilder();

        absolutePath = absolutePath.replace(" ", this.cmdSpaceScape);

        Integer volumeToDevice;
        Configuracoes configuracoes = configService.getConfiguracoes();
        if (nonNull(volumeRemapMin) && nonNull(volumeRemapMax) && (volumeRemapMin < volumeRemapMax)) {
            volumeToDevice = remap(configuracoes.getMasterVolume(),
                    configuracoes.getMasterVolumeMin(),
                    configuracoes.getMasterVolumeMax(),
                    volumeRemapMin, volumeRemapMax);
        } else {
            volumeToDevice = configuracoes.getMasterVolume();
        }

        String cmd = cmdPlayTorre;
        cmd = cmd
                .replace("#musicaPath", absolutePath)
                .replace("#musicaVolume", Integer.toString(volumeToDevice));
        if (repeat) {
            cmd = cmd.replace("#repeat", cmdPlayOptRepeat);
        } else {
            cmd = cmd.replace("#repeat", "");
        }
        if (random) {
            cmd = cmd.replace("#random", cmdPlayOptRandom);
        } else {
            cmd = cmd.replace("#random", "");
        }

        processBuilder.command(cmd.split(","));

        try {
            String cmdInfo = String.format("Executing command: %s", cmd.replace(',', ' '));
            LOGGER.info(cmdInfo);
            processBuilder.redirectErrorStream(true);
            this.process = processBuilder.start();
            this.process.onExit().thenAccept((process) -> this.applicationEventPublisher.publishEvent(new PlayerCommandCompletedEvent(this.info)));
            this.applicationEventPublisher.publishEvent(new PlayerCommandStartedEvent(this.info));
        } catch (IOException e) {
            throw new MusicaException(String.format("Error writing file to output stream. Musica was '%s'",
                    absolutePath), e);
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
        this.applicationEventPublisher.publishEvent(new PlayerCommandStopRequestedEvent(this.info));
        if (this.isTocando()) {
            if (nonNull(this.info)) {
                this.info.appendOutput("Parando a música...");
                this.info.setStatus(TorrePlayerStatus.PARANDO);
            }
            if (stopMode == TorrePlayerStopMode.KILL_PROCESS) {
                killProcess();
            } else if (stopMode == TorrePlayerStopMode.SEND_KEYS) {
                sendKeys(this.stopKeys);
            }
        } else {
            LOGGER.info("Não há música tocando, usando taskkill");
            taskkill();
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
        if (nonNull(this.process)) {
            this.process.destroy();
        }
    }

    private void taskkill() {
        try {
            LOGGER.info("kill: {}", cmdPlayTaskkill);
            Runtime.getRuntime().exec(cmdPlayTaskkill);
        } catch (IOException e) {
            LOGGER.warn("Falha ao matar processo: {}", e.getMessage());
        }
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
