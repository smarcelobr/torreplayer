package br.nom.figueiredo.sergio.torreplayer.model;

public class AgendamentoPlaylist extends Agendamento implements Repeatable, Randomable {
    private Playlist playlist;
    private boolean random = true;
    private boolean repeat = false;

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
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

    @Override
    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
}
