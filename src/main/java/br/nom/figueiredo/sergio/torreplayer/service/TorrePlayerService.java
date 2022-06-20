package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.exception.MusicaException;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class TorrePlayerService {
    Logger logger = LoggerFactory.getLogger(TorrePlayerService.class);

    @Value("${torre.cmd.play}")
    private String cmdPlayTorre;

    @Value("${torre.cmd.spaceEscape}")
    private String cmdSpaceScape;

    private Process process;
    private TorrePlayerInfo info;

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
        String cmd = cmdPlayTorre.replace("#musicaPath", absolutePath);
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
            this.process.destroy();
            if (nonNull(this.info)) {
                this.info.setStatus(TorrePlayerStatus.PARANDO);
            }
        }
    }

    public synchronized TorrePlayerInfo getInfo() {
        TorrePlayerInfo result;
        if (isNull(this.info)) {
            result = new TorrePlayerInfo();
        } else {
            result = this.info;
        }
        try {
            if (nonNull(process)) {
                if (process.isAlive()) {
                    result.setStatus(TorrePlayerStatus.TOCANDO);
                } else {
                    if (isNull(result.getExitValue())) {
                        result.setExitValue(process.exitValue());
                        result.appendOutput(IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8));
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
        } catch (IOException ex) {
            throw new MusicaException("Falhar ao obter info", ex);
        }

        return result;
    }
}
