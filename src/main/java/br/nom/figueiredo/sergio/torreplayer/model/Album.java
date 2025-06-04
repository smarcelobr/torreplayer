package br.nom.figueiredo.sergio.torreplayer.model;

public class Album extends Arquivo {

    @Override
    public TipoMidia getTipo() {
        return TipoMidia.ALBUM;
    }
}
