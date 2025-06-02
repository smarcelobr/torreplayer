package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;
import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoPlaylist;

public class AgendamentoPlaylistStrategy implements AgendamentoStrategy<AgendamentoPlaylist> {
    @Override
    public AgendamentoPlaylist novaInstancia() {
        return new AgendamentoPlaylist();
    }

    @Override
    public boolean isAssignableFrom(Class<? extends Agendamento> aClass) {
        return AgendamentoPlaylist.class.isAssignableFrom(aClass);
    }

    @Override
    public Class<AgendamentoPlaylist> getTargetClass() {
        return AgendamentoPlaylist.class;
    }
}
