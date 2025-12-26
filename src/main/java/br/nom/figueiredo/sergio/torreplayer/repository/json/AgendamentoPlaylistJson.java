package br.nom.figueiredo.sergio.torreplayer.repository.json;

import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoTipo;

public class AgendamentoPlaylistJson extends AgendamentoJson {
    private String playlistNome;
    private boolean repeat;
    private boolean random;

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

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
}
