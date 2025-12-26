package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.*;

public class PlayerTasklet implements Runnable {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlayerTasklet.class);

    private final PlayerCommandService playerCommandService;
    private final Agendamento agendamento;

    public PlayerTasklet(PlayerCommandService playerCommandService, Agendamento agendamento) {
        this.playerCommandService = playerCommandService;
        this.agendamento = agendamento;
    }

    @Override
    public void run() {

        LOGGER.info("Executando agendamento: [{}]", agendamento.getNome());
        try {
            if (agendamento.isAtivo()) {
                switch (agendamento.getTipo()) {
                    case ALBUM:
                        playerCommandService.tocar(((AgendamentoAlbum) agendamento).getAlbum(), ((Repeatable) agendamento).isRepeat(), ((Randomable) agendamento).isRandom());
                        break;
                    case PLAYLIST:
                        playerCommandService.tocar(((AgendamentoPlaylist) agendamento).getPlaylist(), ((Repeatable) agendamento).isRepeat(), ((Randomable) agendamento).isRandom());
                        break;
                    case MUSICA:
                        playerCommandService.tocar(((AgendamentoMusica) agendamento).getMusica(), ((Repeatable) agendamento).isRepeat());
                        break;
                    case PARAR:
                        playerCommandService.stop();
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao executar agendamento id={}", agendamento.getId(), e);
        }

    }
}
