package br.nom.figueiredo.sergio.torreplayer.model;

public class AgendamentoAlbum extends Agendamento implements Repeatable, Randomable {
    private Album album;
    private boolean random = false;
    private boolean repeat = false;

    @Override
    public AgendamentoTipo getTipo() {
        return AgendamentoTipo.ALBUM;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
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
