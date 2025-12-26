package br.nom.figueiredo.sergio.torreplayer.controller.dto;

public class AlbumMusicaDTO {
    private String albumNome;
    private String musicaNome;
    private Boolean repeat;

    public String getAlbumNome() {
        return albumNome;
    }

    public void setAlbumNome(String albumNome) {
        this.albumNome = albumNome;
    }

    public String getMusicaNome() {
        return musicaNome;
    }

    public void setMusicaNome(String musicaNome) {
        this.musicaNome = musicaNome;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }
}
