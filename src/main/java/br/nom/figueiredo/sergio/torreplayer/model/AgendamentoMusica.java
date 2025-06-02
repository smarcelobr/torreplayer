package br.nom.figueiredo.sergio.torreplayer.model;

public class AgendamentoMusica extends Agendamento {
    private Musica musica;

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
}
