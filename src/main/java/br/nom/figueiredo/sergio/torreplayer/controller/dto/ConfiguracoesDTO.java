package br.nom.figueiredo.sergio.torreplayer.controller.dto;

public class ConfiguracoesDTO {
    private Integer masterVolume;
    private String cmdLabel;

    public Integer getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(Integer masterVolume) {
        this.masterVolume = masterVolume;
    }

    public String getCmdLabel() {
        return cmdLabel;
    }

    public void setCmdLabel(String cmdLabel) {
        this.cmdLabel = cmdLabel;
    }
}
