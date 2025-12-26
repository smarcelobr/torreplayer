package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;
import org.slf4j.Logger;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service
public class PlayerSchedulingServiceImpl implements PlayerSchedulingService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlayerSchedulingServiceImpl.class);
    private final TaskScheduler taskScheduler;
    private final PlayerCommandService playerCommandService;
    private final Map<Long, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public PlayerSchedulingServiceImpl(TaskScheduler taskScheduler,
                                       PlayerCommandService playerCommandService) {
        this.taskScheduler = taskScheduler;
        this.playerCommandService = playerCommandService;
    }

    @Override
    public void schedule(Agendamento agendamento) {
        remove(agendamento);
        LOGGER.info("Agendamento nome=[{}] e cron={}", agendamento.getNome(), agendamento.getCronExpression());

        Runnable tasklet = new PlayerTasklet(playerCommandService, agendamento);

        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet,
                new CronTrigger(agendamento.getCronExpression(), TimeZone.getTimeZone(TimeZone.getDefault().getID())));

        jobsMap.put(agendamento.getId(), scheduledTask);
    }

    @Override
    public void remove(Agendamento agendamento) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(agendamento.getId());
        if(scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.put(agendamento.getId(), null);
            LOGGER.info("Agendamento removido: {}", agendamento.getNome());
        }
    }
}
