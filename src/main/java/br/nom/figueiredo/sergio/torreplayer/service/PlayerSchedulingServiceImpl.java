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
    private final TorrePlayerService torrePlayerService;
    private final Map<Long, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public PlayerSchedulingServiceImpl(TaskScheduler taskScheduler, TorrePlayerService torrePlayerService) {
        this.taskScheduler = taskScheduler;
        this.torrePlayerService = torrePlayerService;
    }

    @Override
    public void schedule(Agendamento agendamento) {
        LOGGER.info("Agendamento id={} e cron={}", agendamento.getId(), agendamento.getCronExpression());

        remove(agendamento);
        Runnable tasklet = new PlayerTasklet(torrePlayerService, agendamento);

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
            LOGGER.info("Agendamento id={} removido", agendamento.getId());
        }
    }
}
