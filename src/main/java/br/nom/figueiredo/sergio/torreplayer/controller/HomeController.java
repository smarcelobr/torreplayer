package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final MusicaService musicaService;

    @Autowired
    public HomeController(MusicaService musicaService) {
        this.musicaService = musicaService;
    }

    @GetMapping(value = "/")
    public String listAlbums(Model model) {
        List<Album> albums = musicaService.getAllAlbums();
        model.addAttribute("albums", albums);
        return "index";
    }

    @GetMapping(value = "/album/{albumNome}")
    public String showAlbum(Model model, @PathVariable String albumNome) {
        Album album = musicaService.getAlbumByNome(albumNome);
        List<Musica> musicas = musicaService.getMusicasPorAlbum(album);
        model.addAttribute("album", album);
        model.addAttribute("musicas", musicas);
        return "album";
    }

    @GetMapping(value = "/album/{albumNome}/{musicaNome}", produces = "audio/mpeg")
    public ResponseEntity<byte[]> downloadMusic(@PathVariable String albumNome,
                                                @PathVariable String musicaNome) {
        Album album = musicaService.getAlbumByNome(albumNome);
        Musica musica = musicaService.getMusicaByNome(album, musicaNome);
        try {
            InputStream is = musicaService.getMusicStream(musica);
            byte[] mediaBytes = IOUtils.toByteArray(is);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("audio/mpeg"))
                    .body(mediaBytes);
        } catch (IOException ex) {
            logger.info("Error writing file to output stream. Musica was '{}'", musica.getAbsolutePath(), ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}