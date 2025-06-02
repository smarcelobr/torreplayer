package br.nom.figueiredo.sergio.torreplayer.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AgendamentoMusica.class, name = "MUSICA"),
        @JsonSubTypes.Type(value = AgendamentoPlaylist.class, name = "PLAYLIST"),
        @JsonSubTypes.Type(value = AgendamentoAlbum.class, name = "ALBUM"),
        @JsonSubTypes.Type(value = AgendamentoParar.class, name = "PARAR")
})
public abstract class Agendamento {
    private long id = 0L;
    private String nome;
    private String cronExpression;
    private int ordem;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public abstract AgendamentoTipo getTipo();
}
