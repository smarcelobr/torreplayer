package br.nom.figueiredo.sergio.torreplayer.controller.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Agendamento não encontrado")
public class AgendamentoNaoEncontradoException extends RuntimeException {
    public AgendamentoNaoEncontradoException() {
    }

    public AgendamentoNaoEncontradoException(String message) {
        super(message);
    }

    public AgendamentoNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgendamentoNaoEncontradoException(Throwable cause) {
        super(cause);
    }

    public AgendamentoNaoEncontradoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
