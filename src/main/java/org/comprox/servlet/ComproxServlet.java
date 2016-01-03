package org.comprox.servlet;

import com.google.common.collect.ImmutableList;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.comprox.fragment.source.FragmentSource;
import org.comprox.fragment.source.http.HttpFragmentSource;
import org.comprox.servlet.backend.Backend;
import org.comprox.servlet.backend.passthrough.PassthroughBackend;
import org.comprox.servlet.backend.passthrough.RequestFactory;
import org.comprox.servlet.backend.passthrough.Route;
import org.comprox.servlet.backend.template.TemplateBackend;
import org.comprox.template.TemplateFactory;
import org.comprox.template.parser.TemplateParser;
import org.comprox.template.parser.atto.AttoTemplateParser;
import org.comprox.template.source.TemplateSource;
import org.comprox.template.source.servlet.ServletContextTemplateSource;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class ComproxServlet extends HttpServlet {

    private static final long serialVersionUID = -4458728973722372658L;

    private TemplateFactory templateFactory;
    private CloseableHttpClient passthroughBackendHttpClient;
    private RequestFactory requestFactory;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        TemplateSource templateSource = createTemplateSource(config);
        FragmentSource fragmentSource = createFragmentSource();
        TemplateParser templateParser = new AttoTemplateParser();
        templateFactory = new TemplateFactory(templateSource, fragmentSource, templateParser);

        passthroughBackendHttpClient = HttpClients.createMinimal();

        // TODO fill the request map with route specs
        final Pattern pattern = Pattern.compile("/api/.*");
        Route route = new Route(request -> pattern.matcher(request.getRequestURI()).matches(), "targethost.com", 0);
        requestFactory = new RequestFactory(ImmutableList.of(route));
    }

    private TemplateSource createTemplateSource(ServletConfig config) {
        final String prefix = config.getInitParameter("comprox.template.prefix");
        return new ServletContextTemplateSource(config.getServletContext(), prefix);
    }

    private HttpFragmentSource createFragmentSource() {
        return new HttpFragmentSource(HttpClients.createDefault());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Backend backend;

        if (isPassthroughRequest(request)) {
            backend = new PassthroughBackend(request, response, passthroughBackendHttpClient, requestFactory);
        } else {
            backend = new TemplateBackend(request, response, templateFactory);
        }

        backend.handleRequest();
    }

    private boolean isPassthroughRequest(HttpServletRequest request) {
        final String method = request.getMethod();
        final String contentType = request.getContentType();

        return !"GET".equals(method) || !"text/html".equals(contentType) || !"html".equals(contentType);
    }
}
