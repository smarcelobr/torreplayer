package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.exception.MusicaException;
import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Arquivo;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.model.Playlist;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class MusicaServiceImpl implements MusicaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicaServiceImpl.class);

    @Value(value = "${album.path}")
    private String albumFolder;


    @Override
    public List<Album> getAllAlbums() {
        try (Stream<Path> stream = Files.list(Paths.get(albumFolder))) {
            return stream
                    .filter(Files::isDirectory)
                    .map(this::albumFromPath)
                    .sorted(Comparator.nullsLast(Comparator.comparing(Album::getNome)))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new MusicaException("Ao ler pasta", e);
        }
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        try (Stream<Path> stream = Files.list(Paths.get(albumFolder))) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".m3u"))
                    .map(this::playlistFromPath)
                    .sorted(Comparator.nullsLast(Comparator.comparing(Playlist::getNome)))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new MusicaException("Ao ler pasta", e);
        }
    }

    @Override
    public List<Musica> getMusicasPorAlbum(Album album) {
        Path albumPath = toPath(album);
        LOGGER.debug("Album path: {}", albumPath);
        if (nonNull(albumPath)) {
            try (Stream<Path> stream = Files.list(albumPath)) {
                return stream
                        .filter(path -> !Files.isDirectory(path))
                        .map(this::musicaFromPath)
                        .sorted(Comparator.nullsLast(Comparator.comparing(Musica::getNome)))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                throw new MusicaException("Ao ler album", e);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<Musica> getMusicasPorPlaylist(Playlist playlist) {
        Path playlistPath = toPath(playlist);
        LOGGER.debug("Playlist path: {}", playlistPath);
        if (nonNull(playlistPath)) {
            // abre o arquivo .m3u (playlist) e carrega todas as musicas que lá contem
            try (Stream<String> stream = Files.lines(playlistPath)) {
                return stream
                        .map(String::trim)
                        .filter(line -> !line.startsWith("#"))
                        .filter(line -> !line.isEmpty())
                        .map(Path::of)
                        .map(this::musicaFromPath)
                        .sorted(Comparator.nullsLast(Comparator.comparing(Musica::getNome)))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                LOGGER.error("Falha ao ler playlist.", e);
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Album getAlbumByNome(String albumNome) {
        Path albumPath = Paths.get(albumFolder, albumNome);
        LOGGER.debug("Album path: {}", albumPath);
        if (Files.exists(albumPath) && Files.isDirectory(albumPath)) {
            return albumFromPath(albumPath);
        }
        return null;
    }

    @Override
    public Musica getMusicaByNome(Album album, String musicaNome) {
        Path albumPath = toPath(album);
        if (nonNull(albumPath)) {
            Path musicaPath = albumPath.resolve(musicaNome);
            if (Files.exists(musicaPath) && !Files.isDirectory(musicaPath)) {
                return musicaFromPath(musicaPath);
            }
        }
        return null;
    }

    @Override
    public Playlist getPlaylistByNome(String playlistNome) {
        Path playlistPath = Paths.get(albumFolder, playlistNome);
        if (Files.exists(playlistPath) && !Files.isDirectory(playlistPath)) {
            return playlistFromPath(playlistPath);
        }
        return null;
    }

    private Path toPath(Arquivo arquivo) {
        if (isNull(arquivo)) {
            return null;
        }
        return Paths.get(arquivo.getAbsolutePath());
    }

    private Album albumFromPath(Path path) {
        if (isNull(path)) {
            return null;
        }
        Album album = new Album();
        album.setAbsolutePath(path.toAbsolutePath().toString());
        album.setNome(path.getFileName().toString());
        return album;
    }

    private Playlist playlistFromPath(Path path) {
        if (isNull(path)) {
            return null;
        }
        Playlist playlist = new Playlist();
        playlist.setAbsolutePath(path.toAbsolutePath().toString());
        playlist.setNome(path.getFileName().toString());
        return playlist;
    }

    private Musica musicaFromPath(Path path) {
        if (isNull(path)) {
            return null;
        }
        Path musicaAbsolutePath = path.toAbsolutePath();
        Musica musica = new Musica();
        musica.setAbsolutePath(musicaAbsolutePath.toString());
        musica.setNome(path.getFileName().toString());
        Path albumPath = musicaAbsolutePath.getParent();
        musica.setAlbum(albumFromPath(albumPath));
        return musica;
    }

    @Override
    public InputStream getMusicStream(Musica musica) {
        try {
            Path musicaPath = toPath(musica);
            return nonNull(musicaPath) ? Files.newInputStream(musicaPath) : null;
        } catch (IOException e) {
            throw new MusicaException("Falha ao ler musica.", e);
        }
    }

    @Override
    public Musica postMusica(Album album, String nomeMusica, InputStream musicaContents) {
        if (nonNull(album)) {
            Path pathAlbum = toPath(album);
            if (nonNull(pathAlbum)) {
                Path musicaPath = pathAlbum.resolve(nomeMusica);
                Musica musica = this.musicaFromPath(musicaPath);
                try (OutputStream fileOutput = Files.newOutputStream(musicaPath)) {
                    IOUtils.copy(musicaContents, fileOutput);
                } catch (IOException e) {
                    throw new MusicaException(String.format("Falha ao escrever música %s", musicaPath), e);
                }
                return musica;
            } else {
                throw new MusicaException(String.format("Album %s não existe", album.getNome()));
            }
        } else {
            throw new MusicaException("Album não pode ser nulo");
        }
    }

    @Override
    public Album postAlbum(String albumNome) {
        Path albumPath = Paths.get(albumFolder, albumNome);
        try {
            Files.createDirectory(albumPath);
            return albumFromPath(albumPath);
        } catch (IOException e) {
            throw new MusicaException("Falha ao criar álbum.");
        }
    }

    @Override
    public Album alterarNomeAlbum(String albumNome, String novoNome) {
        Path albumPath = Paths.get(albumFolder, albumNome);
        Path novoAlbumPath = Paths.get(albumFolder, novoNome);
        try {
            Path albumPathRenomeado = Files.move(albumPath, novoAlbumPath);
            return albumFromPath(albumPathRenomeado);
        } catch (IOException e) {
            throw new MusicaException("Falha ao renomear o álbum.");
        }
    }
}
