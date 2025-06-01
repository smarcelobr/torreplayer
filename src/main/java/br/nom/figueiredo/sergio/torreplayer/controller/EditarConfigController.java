package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("config")
public class EditarConfigController {

    private final ConfigService configService;

    @Autowired
    public EditarConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    public String getEditarConfig(Model model) {
        // a classe GlobalModalAttributes já adiciona a configuração ao model
        return "editar_config.html";
    }

    @PostMapping
    public String postEditarConfig(@RequestParam("cmdLabel") String novoCmdLabel,
                                         RedirectAttributes redirectAttributes) {
        configService.setCmdLabel(novoCmdLabel);
        configService.saveConfigFile();

        redirectAttributes.addFlashAttribute("msg",
                "As novas configurações foram salvas.");

        return "redirect:/";
    }

}
