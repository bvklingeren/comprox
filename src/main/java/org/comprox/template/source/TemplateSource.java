package org.comprox.template.source;

import org.comprox.template.TemplateNotFoundException;

import java.io.Reader;

/**
 *
 */
public interface TemplateSource {

    Reader getTemplate(String location) throws TemplateNotFoundException;
}
