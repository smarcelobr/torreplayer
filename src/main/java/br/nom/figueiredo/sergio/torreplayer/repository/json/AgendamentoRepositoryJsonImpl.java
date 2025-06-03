package br.nom.figueiredo.sergio.torreplayer.repository.json;

import br.nom.figueiredo.sergio.torreplayer.model.*;
import br.nom.figueiredo.sergio.torreplayer.repository.AgendamentoRepository;
import br.nom.figueiredo.sergio.torreplayer.repository.MusicaRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AgendamentoRepositoryJsonImpl implements AgendamentoRepository {
    private final File jsonFile;
    private final ObjectMapper objectMapper;
    private final MusicaRepository musicaRepository;

    public AgendamentoRepositoryJsonImpl(@Value("${album.path}") String albumPath,
                                         ObjectMapper objectMapper,
                                         MusicaRepository musicaRepository) {
        this.musicaRepository = musicaRepository;
        Path albumPathObj = Path.of(albumPath);
        this.jsonFile = albumPathObj.resolve("agendamentos.json").toFile();
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Agendamento> findAll() {
        final List<Agendamento> scheduleList;
        ArrayList<AgendamentoJson> lista;
        if (jsonFile.exists()) {
            try {
                lista = objectMapper.readValue(jsonFile, new TypeReference<ArrayList<AgendamentoJson>>() {});
                scheduleList = convertJsonToModel(lista);
            } catch (IOException e) {
                throw new RuntimeException("Error reading agendamentos.json", e);
            }
        } else {
            scheduleList = new ArrayList<>();
        }
        return scheduleList;
    }

    private List<Agendamento> convertJsonToModel(ArrayList<AgendamentoJson> lista) {
        if (lista == null) {
            return new ArrayList<>();
        }
        return lista.stream()
                .map(json -> {
                    Agendamento agendamento;
                    switch (json.getTipo()) {
                        case ALBUM -> {
                            AgendamentoAlbum aa = new AgendamentoAlbum();
                            aa.setAlbum(musicaRepository.getAlbumByNome(((AgendamentoAlbumJson) json).getAlbumNome()));
                            agendamento = aa;
                        }
                        case MUSICA -> {
                            AgendamentoMusica am = new AgendamentoMusica();
                            Album album = musicaRepository.getAlbumByNome(((AgendamentoMusicaJson) json).getAlbumNome());
                            Musica musica = musicaRepository.getMusicaByNome(album, ((AgendamentoMusicaJson) json).getMusicaNome());                            
                            am.setMusica(musica);
                            agendamento = am;
                        }
                        case PLAYLIST -> {
                            AgendamentoPlaylist ap = new AgendamentoPlaylist();
                            Playlist playlist = musicaRepository.getPlaylistByNome(((AgendamentoPlaylistJson) json).getPlaylistNome());                            
                            ap.setPlaylist(playlist);
                            agendamento = ap;
                        }
                        case PARAR -> {
                            agendamento = new AgendamentoParar();
                        }
                        default -> throw new IllegalArgumentException("Tipo de agendamento inválido");
                    }
                    agendamento.setId(json.getId());
                    agendamento.setAtivo(json.isAtivo());
                    agendamento.setNome(json.getNome());
                    agendamento.setCronExpression(json.getCronExpression());
                    agendamento.setOrdem(json.getOrdem());
                    return agendamento;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void salvarLista(List<Agendamento> agendamentoList) {
        try {
            ArrayList<AgendamentoJson> lista = convertModelToJson(agendamentoList);
            objectMapper.writeValue(jsonFile, lista);
        } catch (IOException e) {
            throw new RuntimeException("Error creating agendamentos.json", e);
        }
    }

    private ArrayList<AgendamentoJson> convertModelToJson(List<Agendamento> agendamentoList) {
        if (agendamentoList == null) {
            return new ArrayList<>();
        }
        return agendamentoList.stream()
                .map(agendamento -> {
                    AgendamentoJson json;
                    switch (agendamento.getTipo()) {
                        case ALBUM -> {
                            AgendamentoAlbumJson aa = new AgendamentoAlbumJson();
                            aa.setAlbumNome(((AgendamentoAlbum) agendamento).getAlbum().getNome());
                            json = aa;
                        }
                        case MUSICA -> {
                            AgendamentoMusicaJson am = new AgendamentoMusicaJson();
                            am.setAlbumNome(((AgendamentoMusica) agendamento).getMusica().getAlbum().getNome());
                            am.setMusicaNome(((AgendamentoMusica) agendamento).getMusica().getNome());
                            json = am;
                        }
                        case PLAYLIST -> {
                            AgendamentoPlaylistJson ap = new AgendamentoPlaylistJson();
                            ap.setPlaylistNome(((AgendamentoPlaylist) agendamento).getPlaylist().getNome());
                            json = ap;
                        }
                        case PARAR -> {
                            json = new AgendamentoPararJson();
                        }
                        default -> throw new IllegalArgumentException("Tipo de agendamento inválido");
                    }
                    json.setId(agendamento.getId());
                    json.setAtivo(agendamento.isAtivo());
                    json.setNome(agendamento.getNome());
                    json.setCronExpression(agendamento.getCronExpression());
                    json.setOrdem(agendamento.getOrdem());
                    return json;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }


}
