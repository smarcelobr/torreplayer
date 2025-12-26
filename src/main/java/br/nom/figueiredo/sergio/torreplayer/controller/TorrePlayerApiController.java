package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.controller.dto.AlbumMusicaDTO;
import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import br.nom.figueiredo.sergio.torreplayer.service.PlayerCommandService;
import br.nom.figueiredo.sergio.torreplayer.service.TorrePlayerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("api/torre")
public class TorrePlayerApiController {

    private final MusicaService musicaService;
    private final PlayerCommandService playerCommandService;

    @Autowired
    public TorrePlayerApiController(MusicaService musicaService, PlayerCommandService playerCommandService) {
        this.musicaService = musicaService;
        this.playerCommandService = playerCommandService;
    }

    @GetMapping(value = "info")
    public TorrePlayerInfo info() {
        return playerCommandService.getInfo();
    }

    @PostMapping(value = "play")
    public TorrePlayerInfo play(@RequestBody AlbumMusicaDTO dto) {
        Album album = musicaService.getAlbumByNome(dto.getAlbumNome());
        if (nonNull(dto.getMusicaNome())) {
            Musica musica = musicaService.getMusicaByNome(album, dto.getMusicaNome());
            playerCommandService.tocar(musica, Objects.requireNonNullElse(dto.getRepeat(), false));
        } else {
            playerCommandService.tocar(album, Objects.requireNonNullElse(dto.getRepeat(), false), false);
        }

        return playerCommandService.getInfo();
    }

    @PostMapping(value = "stop")
    public TorrePlayerInfo stop() {
        playerCommandService.stop();
        return playerCommandService.getInfo();
    }

}
