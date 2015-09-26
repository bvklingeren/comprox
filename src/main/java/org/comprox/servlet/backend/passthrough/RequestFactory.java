package org.comprox.servlet.backend.passthrough;

import org.apache.commons.io.Charsets;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public class RequestFactory {

    public HttpUriRequest createPassthroughRequest(HttpServletRequest request) throws IOException {
        final String method = request.getMethod();
        final String backendUri = createBackendUri(request);

        RequestBuilder requestBuilder = RequestBuilder.create(method)
                .setUri(backendUri)
                .setCharset(Charsets.toCharset(request.getCharacterEncoding()));

        copyHeaders(request, requestBuilder);
        copyParameters(request, requestBuilder);
        copyBody(request, requestBuilder);

        return requestBuilder.build();
    }

    private String createBackendUri(HttpServletRequest request) {
        // TODO: request URI must be altered to match a valid backend host
        return request.getRequestURI();
    }

    private void copyHeaders(HttpServletRequest request, RequestBuilder requestBuilder) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();

            Enumeration<String> headers = request.getHeaders(headerName);
            while (headers.hasMoreElements()) {
                requestBuilder.addHeader(headerName, headers.nextElement());
            }
        }
    }

    private void copyParameters(HttpServletRequest request, RequestBuilder requestBuilder) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String key : parameterMap.keySet()) {
            String[] values = parameterMap.get(key);
            for (String value : values) {
                requestBuilder.addParameter(key, value);
            }
        }
    }

    private void copyBody(HttpServletRequest request, RequestBuilder requestBuilder) throws IOException {
        InputStreamEntity entity = new InputStreamEntity(request.getInputStream());

        byte[] bytes = EntityUtils.toByteArray(entity);
        if (bytes.length > 0) {
            requestBuilder.setEntity(new ByteArrayEntity(bytes));
        }
    }
}
