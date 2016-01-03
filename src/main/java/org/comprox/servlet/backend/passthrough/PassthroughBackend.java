package org.comprox.servlet.backend.passthrough;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.comprox.servlet.backend.Backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class PassthroughBackend implements Backend {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassthroughBackend.class);

    private final HttpServletRequest clientRequest;
    private final HttpServletResponse clientResponse;
    private final CloseableHttpClient httpClient;
    private final RequestFactory requestFactory;

    public PassthroughBackend(HttpServletRequest clientRequest, HttpServletResponse clientResponse,
                              CloseableHttpClient httpClient, RequestFactory requestFactory) {
        this.clientRequest = clientRequest;
        this.clientResponse = clientResponse;
        this.httpClient = httpClient;
        this.requestFactory = requestFactory;
    }

    @Override
    public void handleRequest() throws ServletException, IOException {
        LOGGER.debug("Handling passthrough request: " + clientRequest.getRequestURI());

        try {
            HttpUriRequest serverRequest = requestFactory.createPassthroughRequest(clientRequest);
            executePassthroughRequest(serverRequest);

            LOGGER.debug("Successfully handled passthrough request: " + clientRequest.getRequestURI());
        } catch (InvalidRouteException e) {
            LOGGER.warn("No route to backend for passthrough request: {}", clientRequest.getRequestURI(), e);
            clientResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            LOGGER.warn("Failed to successfully handle passthrough request: {}", clientRequest.getRequestURI(), e);
            throw e;
        }
    }

    private void executePassthroughRequest(HttpUriRequest serverRequest) throws IOException, ServletException {
        try (final CloseableHttpResponse serverResponse = httpClient.execute(serverRequest)) {
            copyResponseStatus(serverResponse);
            copyResponseHeaders(serverResponse);
            copyResponseEntity(serverResponse);
        }
    }

    private void copyResponseStatus(CloseableHttpResponse serverResponse) {
        clientResponse.setStatus(serverResponse.getStatusLine().getStatusCode());
    }

    private void copyResponseHeaders(CloseableHttpResponse serverResponse) {
        for (Header header : serverResponse.getAllHeaders()) {
            clientResponse.addHeader(header.getName(), header.getValue());
        }
    }

    private void copyResponseEntity(CloseableHttpResponse serverResponse) throws IOException {
        final HttpEntity entity = serverResponse.getEntity();
        entity.writeTo(clientResponse.getOutputStream());
    }
}
