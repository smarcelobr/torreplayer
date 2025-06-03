package br.nom.figueiredo.sergio.torreplayer.repository;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;

import java.util.List;

public interface AgendamentoRepository {
    List<Agendamento> findAll();

    void salvarLista(List<Agendamento> agendamentoList);
}
