package br.nom.figueiredo.sergio.torreplayer.controller.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Álbum não encontrado")
public class AlbumNaoEncontradoException extends RuntimeException {
    public AlbumNaoEncontradoException() {
    }

    public AlbumNaoEncontradoException(String message) {
        super(message);
    }

    public AlbumNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlbumNaoEncontradoException(Throwable cause) {
        super(cause);
    }

    public AlbumNaoEncontradoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
