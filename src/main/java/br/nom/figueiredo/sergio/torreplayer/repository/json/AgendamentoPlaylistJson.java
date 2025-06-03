package br.nom.figueiredo.sergio.torreplayer.repository.json;

import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoTipo;

public class AgendamentoPlaylistJson extends AgendamentoJson {
    private String playlistNome;

    public String getPlaylistNome() {
        return playlistNome;
    }

    public void setPlaylistNome(String playlistNome) {
        this.playlistNome = playlistNome;
    }

    @Override
    public AgendamentoTipo getTipo() {
        return AgendamentoTipo.PLAYLIST;
    }
}
