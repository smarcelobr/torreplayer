package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;

import java.io.InputStream;
import java.util.List;

public interface MusicaService {

    List<Album> getAllAlbums();
    Album getAlbumByNome(String albumNome);
    List<Musica> getMusicasPorAlbum(Album album);
    Musica getMusicaByNome(Album album, String musicaNome);

    InputStream getMusicStream(Musica musica);

    Musica postMusica(Album album, String nomeMusica, InputStream musicaContents);

    Album postAlbum(String albumNome);
}
