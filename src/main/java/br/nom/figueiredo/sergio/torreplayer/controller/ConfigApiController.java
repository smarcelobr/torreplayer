package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.controller.dto.ConfiguracoesDTO;
import br.nom.figueiredo.sergio.torreplayer.model.Configuracoes;
import br.nom.figueiredo.sergio.torreplayer.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("api/config")
public class ConfigApiController {
    Logger logger = LoggerFactory.getLogger(ConfigApiController.class);

    private final ConfigService configService;

    @Autowired
    public ConfigApiController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping()
    public Configuracoes getConfiguracoes() {
        return configService.getConfiguracoes();
    }

    @PostMapping()
    public Configuracoes setConfiguracoes(@RequestBody ConfiguracoesDTO dto) {
        boolean salvar = false;
        if (nonNull(dto.getMasterVolume())) {
            configService.setVolumePadrao(dto.getMasterVolume());
            salvar=true;
        }
        if (nonNull(dto.getCmdLabel())) {
            configService.setCmdLabel(dto.getCmdLabel());
            salvar=true;
        }
        if (salvar) {
            configService.saveConfigFile();
        }
        return configService.getConfiguracoes();
    }

}
