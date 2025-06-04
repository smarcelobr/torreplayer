package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;
import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoAlbum;

public class AgendamentoAlbumStrategy implements AgendamentoStrategy<AgendamentoAlbum> {
    @Override
    public AgendamentoAlbum novaInstancia() {
        return new AgendamentoAlbum();
    }

    @Override
    public boolean isAssignableFrom(Class<? extends Agendamento> aClass) {
        return AgendamentoAlbum.class.isAssignableFrom(aClass);
    }

    @Override
    public Class<AgendamentoAlbum> getTargetClass() {
        return AgendamentoAlbum.class;
    }
}
