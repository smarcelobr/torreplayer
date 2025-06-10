package br.nom.figueiredo.sergio.torreplayer.repository;

import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.model.Playlist;

import java.io.InputStream;
import java.util.List;

public interface MusicaRepository {

    List<Album> getAllAlbums();
    List<Playlist> getAllPlaylists();
    Album getAlbumByNome(String albumNome);
    List<Musica> getMusicasPorAlbum(Album album);
    List<Musica> getMusicasPorPlaylist(Playlist playlist);
    Musica getMusicaByNome(Album album, String musicaNome);

    InputStream getMusicStream(Musica musica);

    Musica postMusica(Album album, String nomeMusica, InputStream musicaContents);

    Album postAlbum(String albumNome);

    Album alterarNomeAlbum(String albumNome, String novoNome);

    Playlist getPlaylistByNome(String playlistNome);

    void deleteMusica(Musica musica);

    void deleteAlbum(Album album);
}
