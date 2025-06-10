package br.nom.figueiredo.sergio.torreplayer.controller.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Falha inesperada. Consulte os logs.")
public class FalhaInesperadaException extends RuntimeException {
    public FalhaInesperadaException() {
    }

    public FalhaInesperadaException(String message) {
        super(message);
    }

    public FalhaInesperadaException(String message, Throwable cause) {
        super(message, cause);
    }

    public FalhaInesperadaException(Throwable cause) {
        super(cause);
    }

    public FalhaInesperadaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
