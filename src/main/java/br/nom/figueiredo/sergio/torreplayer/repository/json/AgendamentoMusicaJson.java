package br.nom.figueiredo.sergio.torreplayer.repository.json;

import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoTipo;

public class AgendamentoMusicaJson extends AgendamentoJson {
    private String albumNome;
    private String musicaNome;

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

    @Override
    public AgendamentoTipo getTipo() {
        return AgendamentoTipo.MUSICA;
    }
}
