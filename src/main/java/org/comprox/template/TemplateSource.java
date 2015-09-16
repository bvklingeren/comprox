package org.comprox.template;

import java.io.Reader;

/**
 *
 */
public interface TemplateSource {

    Reader getTemplate(String location) throws TemplateNotFoundException;
}
