package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import br.nom.figueiredo.sergio.torreplayer.service.TorrePlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private MusicaService musicaService;

    @Autowired
    private TorrePlayerService torrePlayerService;

    @GetMapping(value = "/")
    public String listAlbums(Model model) {
        List<Album> albums = musicaService.getAllAlbums();
        model.addAttribute("albums", albums);
        return "index.html";
    }

    @GetMapping(value = "/album/{albumNome}")
    public String showAlbum(Model model, @PathVariable String albumNome) {
        Album album = musicaService.getAlbumByNome(albumNome);
        List<Musica> musicas = musicaService.getMusicasPorAlbum(album);
        model.addAttribute("album", album);
        model.addAttribute("musicas", musicas);
        return "album.html";
    }

    @GetMapping(value = "/album/{albumNome}/{musicaNome}", produces = "audio/mpeg")
    public void downloadMusic(@PathVariable String albumNome,
                            @PathVariable String musicaNome,
                            HttpServletResponse response) {
        Album album = musicaService.getAlbumByNome(albumNome);
        Musica musica = musicaService.getMusicaByNome(album, musicaNome);
        try {
            // get your file as InputStream
            InputStream is = musicaService.getMusicStream(musica);
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            logger.info("Error writing file to output stream. Musica was '{}'", musica.getAbsolutePath(), ex);
        }
    }

}
