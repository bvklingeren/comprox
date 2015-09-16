package org.comprox.fragment;

/**
 *
 */
public class InvalidFragmentException extends RuntimeException {

    private static final long serialVersionUID = -5146983926454950884L;

    public InvalidFragmentException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public InvalidFragmentException(String message) {
        super(message);
    }
}
