package org.comprox.servlet;

import org.apache.http.impl.client.HttpClients;
import org.comprox.parser.HtmlRequest;
import org.comprox.parser.HtmlResponse;
import org.comprox.template.TemplateSource;
import org.comprox.template.servlet.ServletContextTemplateSource;
import org.comprox.fragment.source.http.HttpFragmentSource;
import org.comprox.parser.atto.AttoHtmlParser;

import java.io.IOException;
import java.net.URI;
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

    /**
     * Key for content length header.
     */
    private static final String STRING_CONTENT_LENGTH_HEADER_NAME = "Content-Length";
    /**
     * Key for host header
     */
    private static final String STRING_HOST_HEADER_NAME = "Host";

    private AttoHtmlParser htmlParser;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        final TemplateSource templateSource = createTemplateSource(config);
        final HttpFragmentSource fragmentSource = createFragmentSource();

        htmlParser = new AttoHtmlParser(templateSource, fragmentSource);
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
        final String method = request.getMethod();
        if ("GET".equals(method)) {
            doGet(request, response);
        } else {
            passThrough(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final URI uri = URI.create(request.getRequestURI());

        log("Handling request to path: " + uri.getPath());

        htmlParser.parse(new HtmlRequest(uri.getPath()), new HtmlResponse(response.getWriter()));

        log("Finished handling request to path: " + uri.getPath());
    }

    private void passThrough(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log("Passing through request " + request.getPathInfo());
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
}
