package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Arquivo;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class TorrePlayerInfo {
    private Arquivo arquivo;
    private Integer exitValue;
    private String output = StringUtils.EMPTY;
    private TorrePlayerStatus status = TorrePlayerStatus.INDEFINIDO;

    public Arquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(Arquivo path) {
        this.arquivo = Objects.requireNonNull(path);
    }

    public Integer getExitValue() {
        return exitValue;
    }

    public void setExitValue(Integer exitValue) {
        this.exitValue = Objects.requireNonNull(exitValue);
    }

    public String getOutput() {
        return output;
    }

    public void appendOutput(String moreOutput) {
        this.output = this.output + Objects.requireNonNull(moreOutput);
    }

    public void setStatus(TorrePlayerStatus status) {
        this.status = Objects.requireNonNull(status);
    }

    public TorrePlayerStatus getStatus() {
        return status;
    }
}
