package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.controller.dto.AlbumMusicaDTO;
import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import br.nom.figueiredo.sergio.torreplayer.service.TorrePlayerInfo;
import br.nom.figueiredo.sergio.torreplayer.service.TorrePlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("api/torre")
public class TorrePlayerApiController {

    private final MusicaService musicaService;
    private final TorrePlayerService torrePlayerService;

    @Autowired
    public TorrePlayerApiController(MusicaService musicaService, TorrePlayerService torrePlayerService) {
        this.musicaService = musicaService;
        this.torrePlayerService = torrePlayerService;
    }

    @GetMapping(value = "info")
    public TorrePlayerInfo info() {
        return torrePlayerService.getInfo();
    }

    @PostMapping(value = "play")
    public TorrePlayerInfo play(@RequestBody AlbumMusicaDTO dto) {
        Album album = musicaService.getAlbumByNome(dto.getAlbumNome());
        if (nonNull(dto.getMusicaNome())) {
            Musica musica = musicaService.getMusicaByNome(album, dto.getMusicaNome());
            torrePlayerService.tocar(musica);
        } else {
            torrePlayerService.tocar(album, false);
        }

        return torrePlayerService.getInfo();
    }

    @PostMapping(value = "stop")
    public TorrePlayerInfo stop() {
        torrePlayerService.stop();
        return torrePlayerService.getInfo();
    }

}
