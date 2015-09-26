package org.comprox.servlet.backend.passthrough;

import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.UnsupportedEncodingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PassthroughBackendTest {

    @InjectMocks
    private PassthroughBackend backend;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private RequestFactory requestFactory;

    @Mock
    private HttpUriRequest backendRequest;

    @Mock
    private CloseableHttpResponse backendResponse;

    @Mock
    private ServletOutputStream outputStream;

    @Test
    public void testShouldHandleRequestWithResponseBody() throws Exception {
        when(requestFactory.createPassthroughRequest(request)).thenReturn(backendRequest);
        when(httpClient.execute(backendRequest)).thenReturn(backendResponse);

        when(backendResponse.getStatusLine()).thenReturn(statusLineWithStatus(200));
        when(backendResponse.getAllHeaders()).thenReturn(headers());
        when(backendResponse.getEntity()).thenReturn(entityWithBody("body"));
        when(response.getOutputStream()).thenReturn(outputStream);

        backend.handleRequest();

        verify(outputStream).write("body".getBytes());
        verify(response).addHeader("header", "value");
        verify(response).setStatus(200);
    }

    private StatusLine statusLineWithStatus(int statusCode) {
        return new BasicStatusLine(new ProtocolVersion("http", 1, 1), statusCode, null);
    }

    private BasicHeader[] headers() {
        return new BasicHeader[] {new BasicHeader("header", "value")};
    }

    private StringEntity entityWithBody(String body) throws UnsupportedEncodingException {
        return new StringEntity(body);
    }
}