package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.model.Playlist;

public interface PlayerCommandService {
    void tocar(Musica musica, boolean repeat);
    void tocar(Album album, boolean repeat , boolean random);
    void tocar(Playlist playlist, boolean repeat, boolean random);

    void stop();

    TorrePlayerInfo getInfo();
}
