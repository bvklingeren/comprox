package org.comprox.template;

import org.comprox.fragment.source.FragmentSource;
import org.comprox.template.parser.DefaultFragmentFactory;
import org.comprox.template.parser.FragmentFactory;
import org.comprox.template.parser.TemplateParser;
import org.comprox.template.request.ClientRequest;
import org.comprox.template.response.ClientResponse;
import org.comprox.template.source.TemplateSource;

import java.io.Reader;

/**
 *
 */
public class DefaultTemplate implements Template {

    private final TemplateSource templateSource;
    private final FragmentSource fragmentSource;
    private final ClientRequest clientRequest;
    private final TemplateParser templateParser;
    private final ClientResponse clientResponse;

    public DefaultTemplate(TemplateSource templateSource, FragmentSource fragmentSource, TemplateParser templateParser, ClientRequest clientRequest, ClientResponse clientResponse) {
        this.templateSource = templateSource;
        this.fragmentSource = fragmentSource;
        this.clientRequest = clientRequest;
        this.templateParser = templateParser;
        this.clientResponse = clientResponse;
    }

    @Override
    public void render() {
        try {
            final Reader templateReader = templateSource.getTemplate(clientRequest.getLocation());
            final FragmentFactory fragmentFactory = new DefaultFragmentFactory(fragmentSource, clientRequest);

            templateParser.parse(templateReader, fragmentFactory, clientResponse.getWriter());
        } catch (TemplateNotFoundException e) {
            e.printStackTrace();
        }
    }
}
