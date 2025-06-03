package br.nom.figueiredo.sergio.torreplayer.repository.json;

import br.nom.figueiredo.sergio.torreplayer.model.AgendamentoTipo;

public class AgendamentoPararJson extends AgendamentoJson {
    @Override
    public AgendamentoTipo getTipo() {
        return AgendamentoTipo.PARAR;
    }
}
