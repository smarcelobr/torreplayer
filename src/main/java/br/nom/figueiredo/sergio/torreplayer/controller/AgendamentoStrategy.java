package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;

public interface AgendamentoStrategy<T extends Agendamento> {
    T novaInstancia();

    boolean isAssignableFrom(Class<? extends Agendamento> aClass);

    Class<T> getTargetClass();
}
