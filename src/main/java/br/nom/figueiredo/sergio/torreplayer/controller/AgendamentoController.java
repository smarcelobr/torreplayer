package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Agendamento;
import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.model.Musica;
import br.nom.figueiredo.sergio.torreplayer.service.AgendamentoService;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("agendamento")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final MusicaService musicaService;

    public AgendamentoController(AgendamentoService agendamentoService, MusicaService musicaService) {
        this.agendamentoService = agendamentoService;
        this.musicaService = musicaService;
    }

    @GetMapping
    public String agendamento(Model model) {
        model.addAttribute("agendamentos", this.agendamentoService.getAgendamentos());
        return "agendamentos";
    }

    @GetMapping("{albumNome}/{musicaNome}")
    public String agendamentoPorMusica(Model model, @PathVariable String albumNome,
                                       @PathVariable String musicaNome) {

        Album album = musicaService.getAlbumByNome(albumNome);
        Musica musica = musicaService.getMusicaByNome(album, musicaNome);

        Agendamento agendamento = new Agendamento();
        agendamento.setMusica(musica);
        agendamento.setNome("");
        agendamento.setCronExpression("");
        agendamento.setOrdem(this.agendamentoService.getUltimaOrdem() + 1);
        model.addAttribute("agendamento", agendamento);

        return "editar_agenda";
    }

    @GetMapping("{id:\\d+}")
    public String agendamentoPorId(Model model, @PathVariable long id) {
        Agendamento agendamento = this.agendamentoService.getAgendamento(id);
        if (isNull(agendamento)) {
            throw new AgendamentoNaoEncontradoException(String.format("Agendamento %d não encontrado", id));
        }
        model.addAttribute("agendamento", agendamento);

        return "editar_agenda";
    }

    @PostMapping("{id:\\d+}")
    public String salvarAgendamento(Model model, @PathVariable long id,
                                    @RequestParam String albumNome,
                                    @RequestParam String musicaNome,
                                    @RequestParam String nome,
                                    @RequestParam String cronExpression,
                                    RedirectAttributes redirectAttributes) {

        Album album = musicaService.getAlbumByNome(albumNome);
        Musica musica = musicaService.getMusicaByNome(album, musicaNome);
        Agendamento agendamento;

        if (id!=0) {
            agendamento = this.agendamentoService.getAgendamento(id);
            if (isNull(agendamento)) {
                throw new AgendamentoNaoEncontradoException(String.format("Agendamento %d não encontrado", id));
            }
        } else {
            agendamento = new Agendamento();
            agendamento.setOrdem(this.agendamentoService.getUltimaOrdem() + 1);
        }
        agendamento.setMusica(musica);
        agendamento.setNome(nome);
        agendamento.setCronExpression(cronExpression);
        this.agendamentoService.salvarAgendamento(agendamento);

        redirectAttributes.addFlashAttribute("msg",
                String.format("O agendamento %s foi salvo", nome) );

        return "redirect:/agendamento";
    }

    @GetMapping("{id:\\d+}/exclusao")
    public String excluirAgendamentoPorId(Model model, @PathVariable long id) {
        Agendamento agendamento = this.agendamentoService.getAgendamento(id);
        if (isNull(agendamento)) {
            throw new AgendamentoNaoEncontradoException(String.format("Agendamento %d não encontrado", id));
        }
        model.addAttribute("agendamento", agendamento);

        return "excluir_agenda";
    }

    @PostMapping("{id:\\d+}/exclusao")
    public String excluirAgendamento(Model model, @PathVariable long id,
                                    RedirectAttributes redirectAttributes) {
        Agendamento agendamento;

        agendamento = this.agendamentoService.getAgendamento(id);
        if (isNull(agendamento)) {
            throw new AgendamentoNaoEncontradoException(String.format("Agendamento %d não encontrado", id));
        }
        this.agendamentoService.removerAgendamento(agendamento);

        redirectAttributes.addFlashAttribute("msg",
                String.format("O agendamento %s foi removido",agendamento.getNome()));

        return "redirect:/agendamento";
    }

}
