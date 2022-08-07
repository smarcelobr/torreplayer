package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("album/novo-album")
public class NovoAlbumController {

    private final MusicaService musicaService;

    @Autowired
    public NovoAlbumController(MusicaService musicaService) {
        this.musicaService = musicaService;
    }

    @GetMapping
    public String getIncluirMusicaAlbum() {
        return "novo_album.html";
    }

    @PostMapping
    public String postIncluirMusicaAlbum(@RequestParam String nome,
                                         RedirectAttributes redirectAttributes) {
        Album album = musicaService.postAlbum(nome);

        redirectAttributes.addFlashAttribute("msg",
                "O álbum " + album.getNome() + " foi criado");

        return "redirect:/album/"+album.getNome();
    }

}
