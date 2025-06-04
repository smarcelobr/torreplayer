package br.nom.figueiredo.sergio.torreplayer.model;

public class Playlist extends Arquivo {

    @Override
    public TipoMidia getTipo() {
        return TipoMidia.PLAYLIST;
    }
}
