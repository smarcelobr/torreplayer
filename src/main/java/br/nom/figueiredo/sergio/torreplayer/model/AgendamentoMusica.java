package br.nom.figueiredo.sergio.torreplayer.model;

public class AgendamentoMusica extends Agendamento implements Repeatable {
    private Musica musica;
    private boolean repeat = false;

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    @Override
    public AgendamentoTipo getTipo() {
        return AgendamentoTipo.MUSICA;
    }

    @Override
    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
}
