package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AgendamentoServiceImpl implements AgendamentoService {

    private final List<Agendamento> agendamentoList;
    private final File jsonFile;
    private final ObjectMapper objectMapper;

    public AgendamentoServiceImpl(@Value("${album.path}") String albumPath) {
        Path albumPathObj = Path.of(albumPath);
        this.jsonFile = albumPathObj.resolve("agendamentos.json").toFile();
        this.objectMapper = new ObjectMapper();

        agendamentoList = lerListaJson();
    }

    private List<Agendamento> lerListaJson() {
        final List<Agendamento> scheduleList;
        ArrayList<Agendamento> lista;
        if (jsonFile.exists()) {
            try {
                lista = objectMapper.readValue(jsonFile, new TypeReference<ArrayList<Agendamento>>() {});

            } catch (IOException e) {
                throw new RuntimeException("Error reading agendamentos.json", e);
            }
        } else {
            lista = new ArrayList<>();
        }
        return lista;
    }

    private void persistirLista() {
        try {
            objectMapper.writeValue(jsonFile, agendamentoList);
        } catch (IOException e) {
            throw new RuntimeException("Error creating agendamentos.json", e);
        }
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
        persistirLista();
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
        persistirLista();
    }

    @Override
    public int getUltimaOrdem() {
        return agendamentoList.stream()
                .mapToInt(Agendamento::getOrdem)
                .max()
                .orElse(0);
    }
}
