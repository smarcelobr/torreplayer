package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import br.nom.figueiredo.sergio.torreplayer.service.TorrePlayerInfo;
import br.nom.figueiredo.sergio.torreplayer.service.TorrePlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("torre")
public class TorrePlayerController {
    Logger logger = LoggerFactory.getLogger(TorrePlayerController.class);

    @Autowired
    private MusicaService musicaService;

    @Autowired
    private TorrePlayerService torrePlayerService;

    @GetMapping(value = "{albumNome}/{musicaNome}")
    public String playTorre(@PathVariable String albumNome,
                            @PathVariable String musicaNome) {

        Album album = musicaService.getAlbumByNome(albumNome);
        Musica musica = musicaService.getMusicaByNome(album, musicaNome);
        torrePlayerService.tocar(musica);

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
