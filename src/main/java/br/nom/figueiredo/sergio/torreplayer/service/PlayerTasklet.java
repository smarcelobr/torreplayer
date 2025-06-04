package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;
import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoAlbum;
import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoMusica;
import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoPlaylist;

public class PlayerTasklet implements Runnable {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlayerTasklet.class);

    private final TorrePlayerService torrePlayerService;
    private final Agendamento agendamento;

    public PlayerTasklet(TorrePlayerService torrePlayerService, Agendamento agendamento) {
        this.torrePlayerService = torrePlayerService;
        this.agendamento = agendamento;
    }

    @Override
    public void run() {

        try {
            if (agendamento.isAtivo()) {
                switch (agendamento.getTipo()) {
                    case ALBUM:
                        torrePlayerService.tocar(((AgendamentoAlbum) agendamento).getAlbum(), ((AgendamentoAlbum) agendamento).isRandom());
                        break;
                    case PLAYLIST:
                        torrePlayerService.tocar(((AgendamentoPlaylist) agendamento).getPlaylist(), ((AgendamentoPlaylist) agendamento).isRandom());
                        break;
                    case MUSICA:
                        torrePlayerService.tocar(((AgendamentoMusica) agendamento).getMusica());
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao executar agendamento id={}", agendamento.getId(), e);
        }

    }
}
