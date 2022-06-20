package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.exception.MusicaException;
import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
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
    public List<Musica> getMusicasPorAlbum(Album album) {
        Path albumPath = toPath(album);
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
    public Album getAlbumByNome(String albumNome) {
        Path albumPath = Paths.get(albumFolder, albumNome);
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

    private Path toPath(Album album) {
        if (isNull(album)) {
            return null;
        }
        return Paths.get(album.getAbsolutePath());
    }

    private Path toPath(Musica musica) {
        if (isNull(musica)) {
            return null;
        }
        return Paths.get(musica.getAbsolutePath());
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
            return nonNull(musicaPath)?Files.newInputStream(musicaPath):null;
        } catch (IOException e) {
            throw new MusicaException("Falha ao ler musica.", e);
        }
    }

}
