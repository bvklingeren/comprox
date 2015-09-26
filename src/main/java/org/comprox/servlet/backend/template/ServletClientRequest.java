package org.comprox.servlet.backend.template;

import org.comprox.fragment.FragmentRequest;
import org.comprox.template.request.ClientRequest;

import java.net.URI;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 */
class ServletClientRequest implements ClientRequest {

    private final String location;

    public ServletClientRequest(HttpServletRequest request) throws ServletException {
        try {
            this.location = URI.create(request.getRequestURI()).getPath();
        } catch (IllegalArgumentException e) {
            throw new ServletException("Invalid request URI in HttpServletRequest", e);
        }
    }

    @Override
    public FragmentRequest createFragmentRequest(String location) {
        return new FragmentRequest(location);
    }

    @Override
    public String getLocation() {
        return location;
    }
}
