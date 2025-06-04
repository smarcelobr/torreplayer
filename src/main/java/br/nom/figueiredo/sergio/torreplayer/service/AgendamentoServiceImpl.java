package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;
import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoEvento;
import br.nom.figueiredo.sergio.torreplayer.repository.AgendamentoRepository;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class AgendamentoServiceImpl implements AgendamentoService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AgendamentoServiceImpl.class);

    private final AgendamentoRepository repository;
    private final PlayerSchedulingService playerSchedulingService;
    private final List<Agendamento> agendamentoList;

    public AgendamentoServiceImpl(AgendamentoRepository repository,
                                  PlayerSchedulingService playerSchedulingService) {
        this.repository = repository;
        this.playerSchedulingService = playerSchedulingService;

        // carrega todos os agendamentos salvos no banco de dados
        agendamentoList = repository.findAll();
        // cria os jobs deles
        agendamentoList.forEach(a -> {
            try {
                playerSchedulingService.schedule(a);
            } catch (Exception e) {
                LOGGER.error("Erro ao agendar job id={}", a.getId(), e);
            }
        });
    }

    @Override
    public List<Agendamento> getAgendamentos() {
        return Collections.unmodifiableList(agendamentoList);
    }

    @Override
    public Agendamento getAgendamento(long id) {
        return agendamentoList.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removerAgendamento(Agendamento agendamento) {
        try {
            playerSchedulingService.remove(agendamento);
        } catch (Exception e) {
            LOGGER.error("Erro ao remover job id={}", agendamento.getId(), e);
        }
        agendamentoList.removeIf(a -> a.getId() == agendamento.getId());
        repository.salvarLista(agendamentoList);
    }

    @Override
    public void salvarAgendamento(Agendamento agendamento) {
        if (agendamento.getId() == 0L) {
            // gera um novo id para este registro
            // calcula o próximo id disponível
            agendamentoList.stream()
                    .mapToLong(Agendamento::getId)
                    .max()
                    .ifPresentOrElse(lastId -> agendamento.setId(lastId + 1L),
                            () -> agendamento.setId(1L));
        }

        try {
            playerSchedulingService.schedule(agendamento);
        } catch (Exception e) {
            LOGGER.error("Erro ao agendar job id={}", agendamento.getId(), e);
        }

        // se o agendamento com este id não existir, inclui. Caso contrário, atualiza.
        agendamentoList.removeIf(a -> a.getId() == agendamento.getId());
        agendamentoList.add(agendamento);
        repository.salvarLista(agendamentoList);
    }

    @Override
    public int getUltimaOrdem() {
        return agendamentoList.stream()
                .mapToInt(Agendamento::getOrdem)
                .max()
                .orElse(0);
    }

    @Override
    public Boolean getAtivo(long id) {
        Agendamento agendamento = this.getAgendamento(id);
        if (agendamento == null) {
            return false;
        }
        return agendamento.isAtivo();
    }

    @Override
    public void setAtivo(long id, boolean ativo) {
        Agendamento agendamento = this.getAgendamento(id);
        if (agendamento == null) {
            return;
        }
        if (agendamento.isAtivo() != ativo) {
            agendamento.setAtivo(ativo);
            this.salvarAgendamento(agendamento);
        }
    }

    @Override
    public List<AgendamentoEvento> getProximosEventos(long id, int offset, int limit) {
        Agendamento agendamento = this.getAgendamento(id);
        if (agendamento == null) {
            return Collections.emptyList();
        }

        CronExpression cronExpression = CronExpression.parse(agendamento.getCronExpression());

        List<AgendamentoEvento> eventosList = new ArrayList<>();
        LocalDateTime next = LocalDateTime.now();
        for (int i = 0; i < offset+limit; i++) {
            next = cronExpression.next(next);
            if (isNull(next)) {
                break;
            }
            if (i < offset) {
                continue;
            }
            AgendamentoEvento evento = new AgendamentoEvento();
            evento.setAgendamento(agendamento);
            evento.setEvento(next);
            eventosList.add(evento);
        }

        return eventosList;
    }
}
