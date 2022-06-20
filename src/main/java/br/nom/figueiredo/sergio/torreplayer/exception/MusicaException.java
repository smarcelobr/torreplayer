package br.nom.figueiredo.sergio.torreplayer.exception;

public class MusicaException extends RuntimeException {
    public MusicaException() {
    }

    public MusicaException(String message) {
        super(message);
    }

    public MusicaException(String message, Throwable cause) {
        super(message, cause);
    }

    public MusicaException(Throwable cause) {
        super(cause);
    }

    public MusicaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
