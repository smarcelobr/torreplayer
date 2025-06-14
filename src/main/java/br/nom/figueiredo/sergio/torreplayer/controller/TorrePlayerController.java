package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import br.nom.figueiredo.sergio.torreplayer.service.TorrePlayerInfo;
import br.nom.figueiredo.sergio.torreplayer.service.TorrePlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("torre")
public class TorrePlayerController {

    private final MusicaService musicaService;
    private final TorrePlayerService torrePlayerService;

    @Autowired
    public TorrePlayerController(MusicaService musicaService, TorrePlayerService torrePlayerService) {
        this.musicaService = musicaService;
        this.torrePlayerService = torrePlayerService;
    }

    @GetMapping(value = "{albumNome}/{musicaNome}")
    public String playTorre(@PathVariable String albumNome,
                            @PathVariable String musicaNome) {

        Album album = musicaService.getAlbumByNome(albumNome);
        Musica musica = musicaService.getMusicaByNome(album, musicaNome);
        torrePlayerService.tocar(musica);

        return "redirect:/torre/info";
    }

    @GetMapping(value = "album/{albumNome}")
    public String playTorre(@PathVariable String albumNome,
                            @RequestParam(required = false, defaultValue = "false") Boolean random) {

        Album album = musicaService.getAlbumByNome(albumNome);
        torrePlayerService.tocar(album, random);

        return "redirect:/torre/info";
    }

    @GetMapping(value = "info")
    public String acompanhamentoTorre(Model model) {
        TorrePlayerInfo info = torrePlayerService.getInfo();
        model.addAttribute("torrePlayerInfo", info);
        return "torrePlayer/info.html";
    }

    @GetMapping(value = "stop")
    public String stopTorre(Model model) {
        torrePlayerService.stop();
        model.addAttribute("msg", "Parando a música...");
        return "forward:/tocando";
    }

}
