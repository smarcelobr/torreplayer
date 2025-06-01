package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;

import java.util.List;

public interface AgendamentoService {
    List<Agendamento> getAgendamentos();
    Agendamento getAgendamento(long id);
    void removerAgendamento(Agendamento scheduledPlayer);
    void salvarAgendamento(Agendamento scheduledPlayer);

    int getUltimaOrdem();
}
