package org.comprox.template;

/**
 *
 */
public class TemplateException extends RuntimeException {

    private static final long serialVersionUID = 7392410508485760836L;

    public TemplateException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public TemplateException(String message) {
        super(message);
    }
}
