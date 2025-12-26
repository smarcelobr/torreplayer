package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.event.PlayerCommandCompletedEvent;
import br.nom.figueiredo.sergio.torreplayer.event.PlayerCommandStartedEvent;
import br.nom.figueiredo.sergio.torreplayer.event.PlayerCommandStopRequestedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public interface LogEventService {

    @EventListener
    @Async
    void PlayerCommandStartedEvent(PlayerCommandStartedEvent pse);

    @EventListener
    @Async
    void handlePlayerCommandCompleted(PlayerCommandCompletedEvent pse);

    @EventListener
    @Async
    void handle(PlayerCommandStopRequestedEvent pse);

}
