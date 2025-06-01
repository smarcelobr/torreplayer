package br.nom.figueiredo.sergio.torreplayer.model;

public class Configuracoes {

    private Integer masterVolumeMin = 0;
    private Integer masterVolumeMax = 1000;
    private Integer masterVolume =900;

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
}
