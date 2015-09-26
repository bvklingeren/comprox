package org.comprox.template.parser.atto;

import org.attoparser.MarkupParser;
import org.attoparser.ParseException;
import org.attoparser.config.ParseConfiguration;
import org.comprox.template.TemplateException;
import org.comprox.template.parser.FragmentFactory;
import org.comprox.template.parser.TemplateParser;

import java.io.Reader;
import java.io.Writer;

/**
 *
 */
public class AttoTemplateParser implements TemplateParser {

    private final MarkupParser markupParser;

    public AttoTemplateParser() {
        this.markupParser = new MarkupParser(ParseConfiguration.htmlConfiguration());
    }

    public void parse(Reader reader, FragmentFactory fragmentFactory, Writer writer) {
        try {
            markupParser.parse(reader, new FragmentMarkupHandler(markupParser, fragmentFactory, writer));
        } catch (ParseException e) {
            throw new TemplateException("Unable to parse template", e);
        }
    }
}
