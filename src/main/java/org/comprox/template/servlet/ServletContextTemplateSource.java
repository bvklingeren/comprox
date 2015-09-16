package org.comprox.template.servlet;

import org.comprox.template.TemplateSource;
import org.comprox.template.TemplateNotFoundException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.servlet.ServletContext;

/**
 *
 */
public class ServletContextTemplateSource implements TemplateSource {

    private final ServletContext servletContext;
    private final String prefix;

    public ServletContextTemplateSource(ServletContext servletContext, String prefix) {
        this.servletContext = servletContext;
        this.prefix = prefix;
    }

    public Reader getTemplate(String location) throws TemplateNotFoundException {
        String template = getTemplateLocation(location);

        final InputStream inputStream = servletContext.getResourceAsStream(template);

        if (inputStream != null) {
            return new InputStreamReader(inputStream);
        } else {
            throw new TemplateNotFoundException(location);
        }
    }

    private String getTemplateLocation(String location) {
        if (prefix != null) {
            return prefix + location;
        }
        return location;
    }
}
