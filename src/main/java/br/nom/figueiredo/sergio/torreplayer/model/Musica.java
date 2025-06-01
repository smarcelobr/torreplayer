package br.nom.figueiredo.sergio.torreplayer.model;

public class Musica extends Arquivo {
    private Album album;

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Album getAlbum() {
        return album;
    }
}
