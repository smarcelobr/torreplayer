package br.nom.figueiredo.sergio.torreplayer.controller;

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

    @GetMapping
    public String getIncluirMusicaAlbum(Model model, @PathVariable String albumNome) {
        Album album = musicaService.getAlbumByNome(albumNome);
        model.addAttribute("album", album);
        return "incluir_musica.html";
    }

    @PostMapping
    public String postIncluirMusicaAlbum(Model model, @PathVariable String albumNome,
                                         @RequestParam("musica_file") MultipartFile file,
                                         RedirectAttributes redirectAttributes) {

        Album album = musicaService.getAlbumByNome(albumNome);

        try (InputStream fileInputStream = file.getInputStream()) {
            Musica musica = musicaService.postMusica(album, file.getOriginalFilename(), fileInputStream);
            redirectAttributes.addFlashAttribute("msg",
                    "A música " + musica.getNome() + " foi incluída neste álbum");
            return "redirect:/album/"+album.getNome();
        } catch (IOException e) {
            String msg = "Falha ao tentar incluir música " + file.getOriginalFilename() + " neste álbum.";
            logger.info(msg, e);
            redirectAttributes.addFlashAttribute("msg", msg);
            return "redirect:/album/"+album.getNome();
        }

    }

}
