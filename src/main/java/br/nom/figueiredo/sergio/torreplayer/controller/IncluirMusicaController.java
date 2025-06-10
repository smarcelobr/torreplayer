package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.controller.exceptions.AlbumNaoEncontradoException;
import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("album/{albumNome}/incluir-musica")
public class IncluirMusicaController {
    private final Logger logger = LoggerFactory.getLogger(IncluirMusicaController.class);

    private final MusicaService musicaService;

    @Autowired
    public IncluirMusicaController(MusicaService musicaService) {
        this.musicaService = musicaService;
    }

    @ModelAttribute
    public void addAttributes(Model model, @PathVariable String albumNome) {
        Album album = musicaService.getAlbumByNome(albumNome);
        if (album == null) {
            throw new AlbumNaoEncontradoException();
        }
        model.addAttribute("album", album);
    }

    @GetMapping
    public String getIncluirMusicaAlbum() {
        return "incluir_musica";
    }

    @PostMapping
    public String postIncluirMusicaAlbum(Model model, @ModelAttribute Album album,
                                         @RequestParam("musica_file") MultipartFile file,
                                         RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            model.addAttribute("msg",
                    "Música não especificada.");
            return getIncluirMusicaAlbum();
        }


        String redirect = "redirect:/album/%s/editar-album".formatted(album.getNome());
        try (InputStream fileInputStream = file.getInputStream()) {
            Musica musica = musicaService.postMusica(album, file.getOriginalFilename(), fileInputStream);
            redirectAttributes.addFlashAttribute("msg",
                    "A música %s foi incluída neste álbum".formatted(musica.getNome()));
            return redirect;
        } catch (IOException e) {
            String msg = "Falha ao tentar incluir música %s neste álbum.".formatted(file.getOriginalFilename());
            logger.info(msg, e);
            redirectAttributes.addFlashAttribute("msg", msg);
            return redirect;
        }

    }

}
