package org.comprox.template;

import org.comprox.fragment.source.FragmentSource;
import org.comprox.template.parser.TemplateParser;
import org.comprox.template.request.ClientRequest;
import org.comprox.template.response.ClientResponse;
import org.comprox.template.source.TemplateSource;

/**
 *
 */
public class TemplateFactory {

    private final TemplateSource templateSource;
    private final FragmentSource fragmentSource;
    private final TemplateParser templateParser;

    public TemplateFactory(TemplateSource templateSource, FragmentSource fragmentSource, TemplateParser templateParser) {
        this.templateSource = templateSource;
        this.fragmentSource = fragmentSource;
        this.templateParser = templateParser;
    }

    public Template createTemplate(ClientRequest request, ClientResponse response) {
        return new DefaultTemplate(templateSource, fragmentSource, templateParser, request, response);
    }
}
