package br.nom.figueiredo.sergio.torreplayer.repository.json;

import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoTipo;

public class AgendamentoAlbumJson extends AgendamentoJson {
    private String albumNome;

    @Override
    public AgendamentoTipo getTipo() {
        return AgendamentoTipo.ALBUM;
    }

    public String getAlbumNome() {
        return albumNome;
    }

    public void setAlbumNome(String albumNome) {
        this.albumNome = albumNome;
    }
}
