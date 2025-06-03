package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.*;
import br.nom.figueiredo.sergio.torreplayer.service.AgendamentoService;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import org.springframework.scheduling.support.CronExpression;
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

    @GetMapping("parar")
    public String agendamentoDaParada(Model model) {

        AgendamentoParar agendamento = new AgendamentoParar();
        agendamento.setNome("");
        agendamento.setCronExpression("");
        agendamento.setOrdem(this.agendamentoService.getUltimaOrdem() + 1);
        model.addAttribute("agendamento", agendamento);

        return "editar_agenda_parar";
    }

    @GetMapping("{albumNome}/{musicaNome}")
    public String agendamentoPorMusica(Model model, @PathVariable String albumNome,
                                       @PathVariable String musicaNome) {

        Album album = musicaService.getAlbumByNome(albumNome);
        Musica musica = musicaService.getMusicaByNome(album, musicaNome);

        AgendamentoMusica agendamento = new AgendamentoMusica();
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
    public String salvarAgendamento(@PathVariable long id,
                                    @RequestParam AgendamentoTipo tipo,
                                    @RequestParam String albumNome,
                                    @RequestParam String musicaNome,
                                    @RequestParam String playlistNome,
                                    @RequestParam String nome,
                                    @RequestParam String cronExpression,
                                    RedirectAttributes redirectAttributes) {
        validarCronExpression(cronExpression);

        Agendamento agendamento = switch (tipo) {
            case MUSICA -> salvarAgendamentoMusica(id, albumNome, musicaNome, nome, cronExpression);
            case PLAYLIST -> salvarAgendamentoPlaylist(id, playlistNome, nome, cronExpression);
            case PARAR -> salvarAgendamentoParar(id, nome, cronExpression);
            default -> throw new IllegalArgumentException("Tipo de agendamento não reconhecido");
        };

        this.agendamentoService.salvarAgendamento(agendamento);

        redirectAttributes.addFlashAttribute("msg",
                String.format("O agendamento %s foi salvo", nome));

        return "redirect:/agendamento";
    }

    public Agendamento salvarAgendamentoMusica(@PathVariable long id,
                                               @RequestParam String albumNome,
                                               @RequestParam String musicaNome,
                                               @RequestParam String nome,
                                               @RequestParam String cronExpression) {
        AgendamentoMusica agendamento;
        agendamento = getAgendamento(id, nome, cronExpression, new AgendamentoMusicaStrategy());

        Album album = musicaService.getAlbumByNome(albumNome);
        Musica musica = musicaService.getMusicaByNome(album, musicaNome);

        agendamento.setMusica(musica);
        return agendamento;
    }

    public Agendamento salvarAgendamentoPlaylist(@PathVariable long id,
                                                 @RequestParam String playlistNome,
                                                 @RequestParam String nome,
                                                 @RequestParam String cronExpression) {
        AgendamentoPlaylist agendamento;
        agendamento = getAgendamento(id, nome, cronExpression, new AgendamentoPlaylistStrategy());

        Playlist playlist = musicaService.getPlaylistByNome(playlistNome);

        agendamento.setPlaylist(playlist);
        return agendamento;
    }

    public Agendamento salvarAgendamentoParar(@PathVariable long id,
                                              @RequestParam String nome,
                                              @RequestParam String cronExpression) {

        return getAgendamento(id, nome, cronExpression, new AgendamentoPararStrategy());
    }

    private void validarCronExpression(String cronExpression) {
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new IllegalArgumentException("Expressão cron inválida: " + cronExpression);
        }
    }

    private <T extends Agendamento> T getAgendamento(long id, String nome, String cronExpression,
                                                     AgendamentoStrategy<T> agendamentoFactory) {
        T agendamento;
        if (id != 0) {
            Agendamento agendamentoSalvo = this.agendamentoService.getAgendamento(id);
            if (isNull(agendamentoSalvo)) {
                throw new AgendamentoNaoEncontradoException(String.format("Agendamento %d não encontrado", id));
            }
            if (agendamentoFactory.isAssignableFrom(agendamentoSalvo.getClass())) {
                agendamento = agendamentoService.convertAgendamento(agendamentoSalvo, agendamentoFactory.getTargetClass());
            } else {
                // converte o tipo de agendamento salvo para musica
                agendamento = agendamentoFactory.novaInstancia();
                agendamento.setId(agendamentoSalvo.getId());
                agendamento.setOrdem(agendamentoSalvo.getOrdem());
                agendamento.setNome(agendamentoSalvo.getNome());
                agendamento.setCronExpression(agendamentoSalvo.getCronExpression());
            }
        } else {
            agendamento = agendamentoFactory.novaInstancia();
            agendamento.setOrdem(this.agendamentoService.getUltimaOrdem() + 1);
        }
        agendamento.setNome(nome);
        agendamento.setCronExpression(cronExpression);
        return agendamento;
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
    public String excluirAgendamento(@PathVariable long id,
                                     RedirectAttributes redirectAttributes) {
        Agendamento agendamento;

        agendamento = this.agendamentoService.getAgendamento(id);
        if (isNull(agendamento)) {
            throw new AgendamentoNaoEncontradoException(String.format("Agendamento %d não encontrado", id));
        }
        this.agendamentoService.removerAgendamento(agendamento);

        redirectAttributes.addFlashAttribute("msg",
                String.format("O agendamento %s foi removido", agendamento.getNome()));

        return "redirect:/agendamento";
    }

}
