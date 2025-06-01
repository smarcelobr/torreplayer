package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Configuracoes;
import br.nom.figueiredo.sergio.torreplayer.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModalAttributes {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalModalAttributes.class);

    private final ConfigService configService;

    @Autowired
    public GlobalModalAttributes(ConfigService configService) {
        this.configService = configService;
    }

    @ModelAttribute("cfg")
    public Configuracoes getConfiguration() {
        return this.configService.getConfiguracoes();
    }

}
