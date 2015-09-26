package org.comprox.template.parser;

import java.io.Reader;
import java.io.Writer;

/**
 *
 */
public interface TemplateParser {
    void parse(Reader reader, FragmentFactory fragmentFactory, Writer writer);
}
