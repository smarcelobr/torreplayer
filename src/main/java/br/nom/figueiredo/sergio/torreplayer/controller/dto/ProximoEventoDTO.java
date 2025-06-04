package br.nom.figueiredo.sergio.torreplayer.controller.dto;

import java.time.LocalDateTime;

public class ProximoEventoDTO {
    private long agendamentoId;
    private LocalDateTime evento;

    public long getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(long agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public LocalDateTime getEvento() {
        return evento;
    }

    public void setEvento(LocalDateTime evento) {
        this.evento = evento;
    }
}
