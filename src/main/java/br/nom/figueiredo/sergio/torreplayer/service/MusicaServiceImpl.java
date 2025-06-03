package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.model.Playlist;
import br.nom.figueiredo.sergio.torreplayer.repository.MusicaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class MusicaServiceImpl implements MusicaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicaServiceImpl.class);

    private final MusicaRepository repository;

    public MusicaServiceImpl(MusicaRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Album> getAllAlbums() {
        return this.repository.getAllAlbums();
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        return this.repository.getAllPlaylists();
    }

    @Override
    public List<Musica> getMusicasPorAlbum(Album album) {
        return this.repository.getMusicasPorAlbum(album);
    }

    @Override
    public List<Musica> getMusicasPorPlaylist(Playlist playlist) {
        return this.repository.getMusicasPorPlaylist(playlist);
    }

    @Override
    public Album getAlbumByNome(String albumNome) {
        return this.repository.getAlbumByNome(albumNome);
    }

    @Override
    public Musica getMusicaByNome(Album album, String musicaNome) {
        return this.repository.getMusicaByNome(album, musicaNome);
    }

    @Override
    public Playlist getPlaylistByNome(String playlistNome) {
        return this.repository.getPlaylistByNome(playlistNome);
    }

    @Override
    public InputStream getMusicStream(Musica musica) {
        return this.repository.getMusicStream(musica);
    }

    @Override
    public Musica postMusica(Album album, String nomeMusica, InputStream musicaContents) {
        return this.repository.postMusica(album, nomeMusica, musicaContents);
    }

    @Override
    public Album postAlbum(String albumNome) {
        return this.repository.postAlbum(albumNome);
    }

    @Override
    public Album alterarNomeAlbum(String albumNome, String novoNome) {
        return this.repository.alterarNomeAlbum(albumNome, novoNome);
    }
}
