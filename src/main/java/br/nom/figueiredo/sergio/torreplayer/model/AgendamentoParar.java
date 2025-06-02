package br.nom.figueiredo.sergio.torreplayer.model;

public class AgendamentoParar extends Agendamento {
    @Override
    public AgendamentoTipo getTipo() {
        return AgendamentoTipo.PARAR;
    }
}
