package org.comprox.parser.atto;

import org.attoparser.MarkupParser;
import org.attoparser.ParseException;
import org.attoparser.config.ParseConfiguration;
import org.comprox.parser.HtmlRequest;
import org.comprox.parser.HtmlResponse;
import org.comprox.template.TemplateSource;
import org.comprox.template.TemplateNotFoundException;
import org.comprox.fragment.source.FragmentSource;
import org.comprox.parser.HtmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;

/**
 *
 */
public class AttoHtmlParser implements HtmlParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttoHtmlParser.class);

    private final TemplateSource templateSource;
    private final FragmentSource fragmentSource;
    private final MarkupParser markupParser;

    public AttoHtmlParser(TemplateSource templateSource, FragmentSource fragmentSource) {
        this.templateSource = templateSource;
        this.fragmentSource = fragmentSource;
        this.markupParser = new MarkupParser(ParseConfiguration.htmlConfiguration());
    }

    public void parse(HtmlRequest request, HtmlResponse response) {
        final String location = request.getLocation();

        try {
            markupParser.parse(resolveTemplate(location), new FragmentMarkupHandler(request, response, fragmentSource, markupParser));
        } catch (ParseException e) {
            LOGGER.warn("Unable to parse HTML from template at " + location, e);
        } catch (TemplateNotFoundException e) {
            LOGGER.warn("Unable to find template at " + location, e);
        }
    }

    protected Reader resolveTemplate(String location) {
        return templateSource.getTemplate(location);
    }
}
