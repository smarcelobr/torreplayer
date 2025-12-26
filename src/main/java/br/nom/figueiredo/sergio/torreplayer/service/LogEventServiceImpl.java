package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.event.PlayerCommandCompletedEvent;
import br.nom.figueiredo.sergio.torreplayer.event.PlayerCommandStartedEvent;
import br.nom.figueiredo.sergio.torreplayer.event.PlayerCommandStopRequestedEvent;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
public class LogEventServiceImpl implements LogEventService {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LogEventServiceImpl.class);

    @Override
    public void PlayerCommandStartedEvent(PlayerCommandStartedEvent pse) {
        if (nonNull(pse.getInfo()) && nonNull(pse.getInfo().getArquivo())) {
            LOGGER.info("Comando iniciado: [{}]", pse.getInfo().getArquivo().getNome());
        } else {
            LOGGER.info("Comando iniciado.");
        }
    }

    @Override
    public void handlePlayerCommandCompleted(PlayerCommandCompletedEvent pse) {
        if (nonNull(pse.getInfo()) && nonNull(pse.getInfo().getArquivo())) {
            LOGGER.info("Comando concluído: [{}]", pse.getInfo().getArquivo().getNome());
        } else {
            LOGGER.info("Comando concluído.");
        }
    }

    @Override
    public void handle(PlayerCommandStopRequestedEvent pse) {
        if (nonNull(pse.getInfo()) && nonNull(pse.getInfo().getArquivo())) {
            LOGGER.info("Parada requisitada: [{}]", pse.getInfo().getArquivo().getNome());
        } else {
            LOGGER.info("Parada requisitada.");
        }
    }
}
