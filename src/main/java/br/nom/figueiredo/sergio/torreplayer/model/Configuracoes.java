package br.nom.figueiredo.sergio.torreplayer.model;

public class Configuracoes {

    private Integer masterVolumeMin = 0;
    private Integer masterVolumeMax = 1000;
    private Integer masterVolume =900;
    private String cmdLabel = "Externo";

    public Integer getMasterVolumeMin() {
        return masterVolumeMin;
    }

    public void setMasterVolumeMin(Integer masterVolumeMin) {
        this.masterVolumeMin = masterVolumeMin;
    }

    public Integer getMasterVolumeMax() {
        return masterVolumeMax;
    }

    public void setMasterVolumeMax(Integer masterVolumeMax) {
        this.masterVolumeMax = masterVolumeMax;
    }

    /**
     * <p>Valor entre 0 e 1000 que o usuário escolheu como volume.</p>
     * <p>Este valor será remapeado para o range aceito pelo dispositivo de áudio.</p>
     *
     * @return valor entre 0 e 1000 que o usuário escolheu.
     */
    public Integer getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(Integer masterVolume) {
        this.masterVolume = masterVolume;
    }

    public void setCmdLabel(String cmdLabel) {
        this.cmdLabel = cmdLabel;
    }

    /**
     * Label do botão de toque externo. Pode ser "Torre" para
     * indicar que a música será tocada na torre da Igreja ou "Nave"
     * para indicar que a música será tocada na nave da Igreja.
     *
     * @return label do botão de toque externo.
     */
    public String getCmdLabel() {
        return cmdLabel;
    }
}
