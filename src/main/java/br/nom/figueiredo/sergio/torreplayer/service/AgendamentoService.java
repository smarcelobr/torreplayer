package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;

import java.util.List;

public interface AgendamentoService {
    List<Agendamento> getAgendamentos();
    Agendamento getAgendamento(long id);
    void removerAgendamento(Agendamento scheduledPlayer);
    void salvarAgendamento(Agendamento scheduledPlayer);

    int getUltimaOrdem();

    default <T extends Agendamento> T convertAgendamento(Agendamento agendamento, Class<T> targetClass) {
        if (agendamento == null) {
            throw new IllegalArgumentException("Agendamento não pode ser nulo");
        }

        if (targetClass.isInstance(agendamento)) {
            return targetClass.cast(agendamento);
        } else {
            throw new IllegalArgumentException(
                    String.format("Não é possível converter agendamento do tipo %s para %s",
                            agendamento.getClass().getSimpleName(),
                            targetClass.getSimpleName())
            );
        }
    }
}
