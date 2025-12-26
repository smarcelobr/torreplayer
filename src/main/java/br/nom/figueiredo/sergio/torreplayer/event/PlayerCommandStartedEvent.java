package br.nom.figueiredo.sergio.torreplayer.event;

import br.nom.figueiredo.sergio.torreplayer.service.TorrePlayerInfo;

/**
 * Evento que indica que a musica/album/playlist parou de tocar.
 */
public class PlayerCommandStartedEvent {

    private final TorrePlayerInfo info;

    public PlayerCommandStartedEvent(TorrePlayerInfo info) {
        this.info = info;
    }

    public TorrePlayerInfo getInfo() {
        return info;
    }
}
