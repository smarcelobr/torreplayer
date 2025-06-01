package br.nom.figueiredo.sergio.torreplayer.model;

public class Agendamento {
    private long id = 0L;
    private String nome;
    private Musica musica;
    private String cronExpression;
    private int ordem;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
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
}
