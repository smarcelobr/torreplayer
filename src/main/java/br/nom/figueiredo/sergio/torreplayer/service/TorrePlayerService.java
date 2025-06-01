package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Musica;

public interface TorrePlayerService {
    void tocar(Musica musica);

    void stop();

    TorrePlayerInfo getInfo();
}
