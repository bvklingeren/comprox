package org.comprox.fragment;

/**
 *
 */
public class FragmentIOException extends RuntimeException {

    private static final long serialVersionUID = 4646862343475036227L;

    public FragmentIOException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public FragmentIOException(Throwable throwable) {
        super(throwable);
    }
}
