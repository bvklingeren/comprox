package org.comprox.servlet.backend.passthrough;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;

public class Route {

    private final String targetHost;
    private final int targetPort;
    private final RequestMatcher requestMatcher;

    public Route(final RequestMatcher requestMatcher, final String targetHost, int targetPort) {
        this.requestMatcher = requestMatcher;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }

    public boolean matches(final HttpServletRequest request) {
        return requestMatcher.matches(request);
    }

    public URI createUri(final HttpServletRequest request) {
        try {
            final URIBuilder uriBuilder = new URIBuilder()
                    .setScheme(request.getScheme())
                    .setHost(targetHost)
                    .setPath(request.getRequestURI())
                    .setCustomQuery(request.getQueryString());

            if (targetPort > 0) {
                uriBuilder.setPort(targetPort);
            }

            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new InvalidRouteException("Failed to create URI from request with target host " + targetHost, e);
        }
    }

    public interface RequestMatcher {
        boolean matches(HttpServletRequest request);
    }
}
