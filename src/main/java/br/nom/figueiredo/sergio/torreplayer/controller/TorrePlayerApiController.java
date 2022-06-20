package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.controller.dto.AlbumMusicaDTO;
import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import br.nom.figueiredo.sergio.torreplayer.service.TorrePlayerInfo;
import br.nom.figueiredo.sergio.torreplayer.service.TorrePlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/torre")
public class TorrePlayerApiController {
    Logger logger = LoggerFactory.getLogger(TorrePlayerApiController.class);

    @Autowired
    private MusicaService musicaService;

    @Autowired
    private TorrePlayerService torrePlayerService;

    @GetMapping(value = "info")
    public TorrePlayerInfo info() {
        return torrePlayerService.getInfo();
    }

    @PostMapping(value = "play")
    public TorrePlayerInfo play(@RequestBody AlbumMusicaDTO dto) {
        Album album = musicaService.getAlbumByNome(dto.getAlbumNome());
        Musica musica = musicaService.getMusicaByNome(album, dto.getMusicaNome());
        torrePlayerService.tocar(musica);

        return torrePlayerService.getInfo();
    }

    @PostMapping(value = "stop")
    public TorrePlayerInfo stop() {
        torrePlayerService.stop();
        return torrePlayerService.getInfo();
    }

}
