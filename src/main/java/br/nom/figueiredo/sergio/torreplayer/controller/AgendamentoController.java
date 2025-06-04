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
import static java.util.Objects.nonNull;

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

        return "editar_agenda";
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

    @GetMapping("album/{albumNome}")
    public String agendamentoPorAlbum(Model model, @PathVariable String albumNome) {

        Album album = musicaService.getAlbumByNome(albumNome);

        AgendamentoAlbum agendamento = new AgendamentoAlbum();
        agendamento.setAlbum(album);
        agendamento.setRandom(true);
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
                                    @RequestParam(required = false) String albumNome,
                                    @RequestParam(required = false) String musicaNome,
                                    @RequestParam(required = false) String playlistNome,
                                    @RequestParam(required = false) Boolean random,
                                    @RequestParam String nome,
                                    @RequestParam String cronExpression,
                                    RedirectAttributes redirectAttributes) {
        Boolean ativo = null;
        random = nonNull(random) && random;

        validarCronExpression(cronExpression);

        Agendamento agendamento = switch (tipo) {
            case MUSICA -> salvarAgendamentoMusica(id, albumNome, musicaNome, ativo, nome, cronExpression);
            case ALBUM -> salvarAgendamentoAlbum(id, albumNome, random, ativo, nome, cronExpression);
            case PLAYLIST -> salvarAgendamentoPlaylist(id, playlistNome, random, ativo, nome, cronExpression);
            case PARAR -> salvarAgendamentoParar(id, ativo, nome, cronExpression);
            default -> throw new IllegalArgumentException("Tipo de agendamento não reconhecido");
        };

        this.agendamentoService.salvarAgendamento(agendamento);

        redirectAttributes.addFlashAttribute("msg",
                String.format("O agendamento %s foi salvo", nome));

        return "redirect:/agendamento";
    }

    public Agendamento salvarAgendamentoMusica(long id,
                                               String albumNome,
                                               String musicaNome,
                                               Boolean ativo, String nome,
                                               String cronExpression) {
        AgendamentoMusica agendamento;
        agendamento = getAgendamento(id, ativo, nome, cronExpression, new AgendamentoMusicaStrategy());

        Album album = musicaService.getAlbumByNome(albumNome);
        Musica musica = musicaService.getMusicaByNome(album, musicaNome);

        agendamento.setMusica(musica);
        return agendamento;
    }

    public Agendamento salvarAgendamentoAlbum(long id,
                                                 String albumNome,
                                                 Boolean random,
                                                 Boolean ativo,
                                                 String nome,
                                                 String cronExpression) {
        AgendamentoAlbum agendamento;
        agendamento = getAgendamento(id, ativo, nome, cronExpression, new AgendamentoAlbumStrategy());

        Album album = musicaService.getAlbumByNome(albumNome);

        agendamento.setAlbum(album);
        agendamento.setRandom(random);
        return agendamento;
    }

    public Agendamento salvarAgendamentoPlaylist(long id,
                                                 String playlistNome,
                                                 Boolean random,
                                                 Boolean ativo,
                                                 String nome,
                                                 String cronExpression) {
        AgendamentoPlaylist agendamento;
        agendamento = getAgendamento(id, ativo, nome, cronExpression, new AgendamentoPlaylistStrategy());

        Playlist playlist = musicaService.getPlaylistByNome(playlistNome);

        agendamento.setPlaylist(playlist);
        agendamento.setRandom(random);
        return agendamento;
    }

    public Agendamento salvarAgendamentoParar(long id,
                                              Boolean ativo,
                                              String nome,
                                              String cronExpression) {

        return getAgendamento(id, ativo, nome, cronExpression, new AgendamentoPararStrategy());
    }

    private void validarCronExpression(String cronExpression) {
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new IllegalArgumentException("Expressão cron inválida: " + cronExpression);
        }
    }

    private <T extends Agendamento> T getAgendamento(long id, Boolean ativo, String nome, String cronExpression,
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
                agendamento.setAtivo(agendamentoSalvo.isAtivo());
                agendamento.setOrdem(agendamentoSalvo.getOrdem());
                agendamento.setNome(agendamentoSalvo.getNome());
                agendamento.setCronExpression(agendamentoSalvo.getCronExpression());
            }
        } else {
            agendamento = agendamentoFactory.novaInstancia();
            agendamento.setOrdem(this.agendamentoService.getUltimaOrdem() + 1);
        }
        if (nonNull(ativo)) {
            agendamento.setAtivo(ativo);
        }
        if (nonNull(nome) && !nome.isBlank()) {
            agendamento.setNome(nome);
        }
        if (nonNull(cronExpression) && !cronExpression.isBlank()) {
            agendamento.setCronExpression(cronExpression);
        }
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
