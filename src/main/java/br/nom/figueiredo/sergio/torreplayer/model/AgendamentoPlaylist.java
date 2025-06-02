package br.nom.figueiredo.sergio.torreplayer.model;

public class AgendamentoPlaylist extends Agendamento {
    private Playlist playlist;

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public AgendamentoTipo getTipo() {
        return AgendamentoTipo.PLAYLIST;
    }
}
