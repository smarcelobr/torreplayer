package br.nom.figueiredo.sergio.torreplayer.model;

public abstract class Arquivo {
    private String absolutePath;
    private String nome;

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public abstract TipoMidia getTipo();
}
