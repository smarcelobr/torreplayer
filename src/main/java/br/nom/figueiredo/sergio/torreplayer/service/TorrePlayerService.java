package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.model.Playlist;

public interface TorrePlayerService {
    void tocar(Musica musica);
    void tocar(Album album, Boolean random);

    void stop();

    TorrePlayerInfo getInfo();

    void tocar(Playlist playlist, boolean random);
}
