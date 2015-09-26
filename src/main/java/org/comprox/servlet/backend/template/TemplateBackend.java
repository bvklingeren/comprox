package org.comprox.servlet.backend.template;

import org.comprox.servlet.backend.Backend;
import org.comprox.template.Template;
import org.comprox.template.TemplateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class TemplateBackend implements Backend {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateBackend.class);

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final TemplateFactory templateFactory;

    public TemplateBackend(HttpServletRequest request, HttpServletResponse response, TemplateFactory templateFactory) {
        this.request = request;
        this.response = response;
        this.templateFactory = templateFactory;
    }

    @Override
    public void handleRequest() throws ServletException, IOException {
        LOGGER.debug("Handling template request: " + request.getRequestURI());

        try {
            Template template = templateFactory.createTemplate(new ServletClientRequest(request), new ServletClientResponse(response));
            template.render();
        } catch (Exception e) {
            LOGGER.warn("Failed to successfully handle template request: {}", request.getRequestURI(), e);
            throw new ServletException(e);
        }

        LOGGER.debug("Successfully handled template request: " + request.getRequestURI());
    }
}
