package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;
import br.nom.figueiredo.sergio.torreplayer.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository repository;
    private final List<Agendamento> agendamentoList;

    public AgendamentoServiceImpl(AgendamentoRepository repository) {
        this.repository = repository;

        agendamentoList = repository.findAll();
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
                            ()-> agendamento.setId(1L));
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
}
