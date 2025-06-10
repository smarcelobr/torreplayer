package br.nom.figueiredo.sergio.torreplayer.controller.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Música não encontrada")
public class MusicaNaoEncontradaException extends RuntimeException {
    public MusicaNaoEncontradaException() {
    }

    public MusicaNaoEncontradaException(String message) {
        super(message);
    }

    public MusicaNaoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }

    public MusicaNaoEncontradaException(Throwable cause) {
        super(cause);
    }

    public MusicaNaoEncontradaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
