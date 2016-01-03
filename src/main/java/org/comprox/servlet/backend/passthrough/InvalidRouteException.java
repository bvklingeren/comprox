package org.comprox.servlet.backend.passthrough;

public class InvalidRouteException extends RuntimeException {

    private static final long serialVersionUID = -6212289318455392975L;

    public InvalidRouteException(final String message) {
        super(message);
    }

    public InvalidRouteException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
