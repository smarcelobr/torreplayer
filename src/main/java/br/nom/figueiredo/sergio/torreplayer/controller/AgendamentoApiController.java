package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.controller.dto.AgendamentoAtivoDTO;
import br.nom.figueiredo.sergio.torreplayer.controller.dto.ProximoEventoDTO;
import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoEvento;
import br.nom.figueiredo.sergio.torreplayer.service.AgendamentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/agendamento")
public class AgendamentoApiController {
    Logger logger = LoggerFactory.getLogger(AgendamentoApiController.class);

    private final AgendamentoService agendamentoService;

    @Autowired
    public AgendamentoApiController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping("{id:\\d+}/ativo")
    public Boolean getAtivo(@PathVariable(value = "id") long id) {
        return agendamentoService.getAtivo(id);
    }

    @PutMapping("{id:\\d+}/ativo")
    public boolean setAtivo(@PathVariable(value = "id") long id,
                                @RequestBody AgendamentoAtivoDTO agendamentoAtivoDTO) {

        agendamentoService.setAtivo(id, agendamentoAtivoDTO.isAtivo());
        return agendamentoAtivoDTO.isAtivo();
    }

    @GetMapping("{id:\\d+}/next")
    public List<ProximoEventoDTO> getNext(@PathVariable(value = "id") long id,
                                          @RequestParam(required = false, defaultValue = "0") int offset,
                                          @RequestParam(required = false, defaultValue = "5") int limit) {
        List<AgendamentoEvento> proximosEventos = agendamentoService.getProximosEventos(id, offset, limit);
        return convertModelToDto(proximosEventos);
    }

    private List<ProximoEventoDTO> convertModelToDto(List<AgendamentoEvento> proximosEventos) {
        return proximosEventos.stream()
                .map(proximoEvento -> {
                    ProximoEventoDTO dto = new ProximoEventoDTO();
                    dto.setAgendamentoId(proximoEvento.getAgendamento().getId());
                    dto.setEvento(proximoEvento.getEvento());
                    return dto;
                }).toList();
    }

}
