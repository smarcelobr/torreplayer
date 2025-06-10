package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.controller.exceptions.AlbumNaoEncontradoException;
import br.nom.figueiredo.sergio.torreplayer.controller.exceptions.FalhaInesperadaException;
import br.nom.figueiredo.sergio.torreplayer.model.Album;
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
@RequestMapping("album/{albumNome}/exclusao")
public class ExcluirAlbumController {
    private final Logger logger = LoggerFactory.getLogger(ExcluirAlbumController.class);

    private final MusicaService musicaService;

    @Autowired
    public ExcluirAlbumController(MusicaService musicaService) {
        this.musicaService = musicaService;
    }

    @ModelAttribute
    public void addAttributes(Model model, @PathVariable String albumNome) {
        Album album = musicaService.getAlbumByNome(albumNome);
        if (isNull(album)) {
            throw new AlbumNaoEncontradoException();
        }
        model.addAttribute("album", album);
    }

    @GetMapping
    public String getExcluirMusicaAlbum() {
        return "excluir_album";
    }

    @PostMapping
    public String excluirAgendamento(@ModelAttribute Album album,
                                     RedirectAttributes redirectAttributes) {

        try {
            musicaService.excluirAlbumCompleto(album);
        } catch (Exception ex) {
            throw new FalhaInesperadaException(ex);
        }

        redirectAttributes.addFlashAttribute("msg",
                String.format("O album %s foi removido", album.getNome()));

        return "redirect:/";
    }

}
