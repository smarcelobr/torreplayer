package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;
import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoMusica;

public class AgendamentoMusicaStrategy implements AgendamentoStrategy<AgendamentoMusica> {
    @Override
    public AgendamentoMusica novaInstancia() {
        return new AgendamentoMusica();
    }

    @Override
    public boolean isAssignableFrom(Class<? extends Agendamento> aClass) {
        return AgendamentoMusica.class.isAssignableFrom(aClass);
    }

    @Override
    public Class<AgendamentoMusica> getTargetClass() {
        return AgendamentoMusica.class;
    }
}
