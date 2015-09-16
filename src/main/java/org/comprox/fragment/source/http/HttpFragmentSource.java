package org.comprox.fragment.source.http;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.comprox.fragment.FragmentRequest;
import org.comprox.fragment.FragmentResponse;
import org.comprox.fragment.InvalidFragmentException;
import org.comprox.fragment.source.FragmentSource;
import org.comprox.fragment.source.FragmentSourceChain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;

/**
 *
 */
public class HttpFragmentSource extends FragmentSourceChain {

    private final CloseableHttpClient httpClient;

    public HttpFragmentSource(CloseableHttpClient httpClient) {
        this(null, httpClient);
    }

    public HttpFragmentSource(FragmentSource next, CloseableHttpClient httpClient) {
        super(next);
        this.httpClient = httpClient;
    }

    @Override
    public FragmentResponse fetch(final FragmentRequest request) {
        final String fragmentLocation = request.getLocation();

        final HttpGet get = createHttpRequest(request);

        try (final CloseableHttpResponse response = httpClient.execute(get)) {
            final StatusLine statusLine = response.getStatusLine();

            switch (statusLine.getStatusCode()) {
                case HttpStatus.SC_OK:
                    final byte[] bytes = EntityUtils.toByteArray(response.getEntity());
                    return new HttpFragmentResponse(new ByteArrayInputStream(bytes));
                case HttpStatus.SC_NOT_FOUND:
                    return getNext().fetch(request);
                default:
                    final String message = String.format("Unexpected HTTP status received (%s) with reason '%s', " +
                            "while getting template at %s", statusLine.getStatusCode(), statusLine.getReasonPhrase(),
                            get.getURI().toString());
                    throw new InvalidFragmentException(message);
            }
        } catch (IOException e) {
            throw new InvalidFragmentException("Failed to get template at " + fragmentLocation, e);
        }
    }

    private HttpGet createHttpRequest(FragmentRequest request) {
        final String location = request.getLocation();

        try {
            URI uri = URI.create(location);
            return new HttpGet(uri);
        } catch (IllegalArgumentException e) {
            throw new InvalidFragmentException("Failed to get template at " + location, e);
        }
    }
}
