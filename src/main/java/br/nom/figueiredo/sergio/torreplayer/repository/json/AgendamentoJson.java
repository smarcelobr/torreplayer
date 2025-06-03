package br.nom.figueiredo.sergio.torreplayer.repository.json;

import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoTipo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AgendamentoMusicaJson.class, name = "MUSICA"),
        @JsonSubTypes.Type(value = AgendamentoPlaylistJson.class, name = "PLAYLIST"),
        @JsonSubTypes.Type(value = AgendamentoAlbumJson.class, name = "ALBUM"),
        @JsonSubTypes.Type(value = AgendamentoPararJson.class, name = "PARAR")
})
public abstract class AgendamentoJson {
    private long id = 0L;
    private boolean ativo = false;
    private String nome;
    private String cronExpression;
    private int ordem;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
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
