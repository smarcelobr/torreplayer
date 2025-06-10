package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.controller.exceptions.AlbumNaoEncontradoException;
import br.nom.figueiredo.sergio.torreplayer.controller.exceptions.FalhaInesperadaException;
import br.nom.figueiredo.sergio.torreplayer.controller.exceptions.MusicaNaoEncontradaException;
import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("album/{albumNome}/{musicaNome}/exclusao")
public class ExcluirMusicaController {
    private final Logger logger = LoggerFactory.getLogger(ExcluirMusicaController.class);

    private final MusicaService musicaService;

    @Autowired
    public ExcluirMusicaController(MusicaService musicaService) {
        this.musicaService = musicaService;
    }

    @ModelAttribute
    public void addAttributes(Model model, @PathVariable String albumNome, @PathVariable String musicaNome) {
        Album album = musicaService.getAlbumByNome(albumNome);
        if (isNull(album)) {
            throw new AlbumNaoEncontradoException();
        }
        Musica musica = musicaService.getMusicaByNome(album, musicaNome);
        if (isNull(musica)) {
            throw new MusicaNaoEncontradaException();
        }
        model.addAttribute("album", album);
        model.addAttribute("musica", musica);
    }

    @GetMapping
    public String getExcluirMusicaAlbum() {
        return "excluir_musica";
    }

    @PostMapping
    public String excluirAgendamento(@ModelAttribute Album album,
                                     @ModelAttribute Musica musica,
                                     RedirectAttributes redirectAttributes) {

        try {
            musicaService.excluirDoAlbum(album, musica);
        } catch (Exception ex) {
            throw new FalhaInesperadaException(ex);
        }

        redirectAttributes.addFlashAttribute("msg",
                String.format("A música %s foi removida do álbum %s", musica.getNome(), album.getNome()));

        return String.format("redirect:/album/%s/editar-album",album.getNome());
    }

}
