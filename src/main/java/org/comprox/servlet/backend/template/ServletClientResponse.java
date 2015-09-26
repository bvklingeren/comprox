package org.comprox.servlet.backend.template;

import org.comprox.template.response.ClientResponse;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
class ServletClientResponse implements ClientResponse {

    private final Writer writer;

    public ServletClientResponse(HttpServletResponse response) throws ServletException {
        try {
            this.writer = response.getWriter();
        } catch (IOException e) {
            throw new ServletException("Failed to get response writer from HttpServletResponse", e);
        }
    }

    @Override
    public Writer getWriter() {
        return writer;
    }
}
