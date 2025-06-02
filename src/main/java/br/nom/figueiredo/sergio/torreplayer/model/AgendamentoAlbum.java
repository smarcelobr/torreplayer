package br.nom.figueiredo.sergio.torreplayer.model;

public class AgendamentoAlbum extends Agendamento {
    private Album album;

    @Override
    public AgendamentoTipo getTipo() {
        return AgendamentoTipo.ALBUM;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
