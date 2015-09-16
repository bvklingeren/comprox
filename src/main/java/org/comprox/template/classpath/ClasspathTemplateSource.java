package org.comprox.template.classpath;

import org.comprox.template.TemplateSource;
import org.comprox.template.TemplateNotFoundException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 *
 */
public class ClasspathTemplateSource implements TemplateSource {

    public Reader getTemplate(String location) {
        final InputStream inputStream = getClass().getResourceAsStream("/" + location);
        if (inputStream != null) {
            return new InputStreamReader(inputStream);
        } else {
            throw new TemplateNotFoundException(location);
        }
    }
}
