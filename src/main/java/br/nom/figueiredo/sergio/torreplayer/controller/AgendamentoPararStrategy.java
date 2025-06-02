package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;
import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoParar;

public class AgendamentoPararStrategy implements AgendamentoStrategy<AgendamentoParar> {
    @Override
    public AgendamentoParar novaInstancia() {
        return new AgendamentoParar();
    }

    @Override
    public boolean isAssignableFrom(Class<? extends Agendamento> aClass) {
        return AgendamentoParar.class.isAssignableFrom(aClass);
    }

    @Override
    public Class<AgendamentoParar> getTargetClass() {
        return AgendamentoParar.class;
    }
}
