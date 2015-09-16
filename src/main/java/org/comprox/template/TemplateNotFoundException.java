package org.comprox.template;

/**
 *
 */
public class TemplateNotFoundException extends TemplateException {

    private static final long serialVersionUID = -7995412504774083024L;

    public TemplateNotFoundException(String location, Throwable throwable) {
        super(createMessage(location), throwable);
    }

    public TemplateNotFoundException(String location) {
        super(createMessage(location));
    }

    private static String createMessage(String location) {
        return "Template at " + location + " could not be found";
    }
}
