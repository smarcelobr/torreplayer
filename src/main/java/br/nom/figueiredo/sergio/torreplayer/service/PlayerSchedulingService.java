package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;

public interface PlayerSchedulingService {
    void schedule(Agendamento agendamento);
    void remove(Agendamento agendamento);
}
