package br.nom.figueiredo.sergio.torreplayer.model;

import java.time.LocalDateTime;

public class AgendamentoEvento {
    private Agendamento agendamento;
    private LocalDateTime evento;

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public LocalDateTime getEvento() {
        return evento;
    }

    public void setEvento(LocalDateTime evento) {
        this.evento = evento;
    }
}
