package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("album/{albumNome}/editar-album")
public class EditarAlbumController {

    private final MusicaService musicaService;

    @Autowired
    public EditarAlbumController(MusicaService musicaService) {
        this.musicaService = musicaService;
    }

    @GetMapping
    public String getEditarMusicaAlbum(Model model, @PathVariable String albumNome) {
        Album album = musicaService.getAlbumByNome(albumNome);
        List<Musica> musicas = musicaService.getMusicasPorAlbum(album);
        model.addAttribute("album", album);
        model.addAttribute("musicas", musicas);
        return "editar_album.html";
    }

    @PostMapping("nome")
    public String postEditarNomeAlbum(@PathVariable String albumNome,
                                      @RequestParam String novoNome,
                                         RedirectAttributes redirectAttributes) {
        Album album = musicaService.alterarNomeAlbum(albumNome, novoNome);

        redirectAttributes.addFlashAttribute("msg",
                "O nome do álbum " + albumNome + " foi alterado para " + album.getNome() );

        return "redirect:/album/"+album.getNome();
    }

}
