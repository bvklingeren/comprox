package org.comprox.servlet.backend.passthrough;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RequestFactoryTest {

    private final RequestFactory requestFactory = new RequestFactory();

    @Mock
    private HttpServletRequest clientRequest;

    @Test
    public void testShouldCreateBasicGetRequest() throws Exception {
        String uri = "http://host/path";

        MockHttpServletRequest get = new MockHttpServletRequest("GET", uri);

        HttpUriRequest backendRequest = requestFactory.createPassthroughRequest(get);

        assertThat(backendRequest.getMethod(), is("GET"));
        assertThat(backendRequest.getURI(), is(URI.create(uri)));
    }

    @Test
    public void testShouldCreateGetRequestWithHeaders() throws Exception {
        String uri = "http://host/path";

        MockHttpServletRequest get = new MockHttpServletRequest("GET", uri);
        get.addHeader("header", "value");

        HttpUriRequest backendRequest = requestFactory.createPassthroughRequest(get);

        assertThat(backendRequest.getMethod(), is("GET"));
        assertThat(backendRequest.getURI(), is(URI.create(uri)));

        Header[] headers = backendRequest.getHeaders("header");
        assertThat(headers.length, is(1));
        assertThat(headers[0].getValue(), is("value"));
    }

    @Test
    public void testShouldCreateGetRequestWithParameters() throws Exception {
        String uri = "http://host/path";

        MockHttpServletRequest get = new MockHttpServletRequest("GET", uri);
        get.addParameter("parameter", new String[]{"value1", "value2"});

        HttpUriRequest backendRequest = requestFactory.createPassthroughRequest(get);

        assertThat(backendRequest.getMethod(), is("GET"));

        URI newUri = new URIBuilder(uri).addParameter("parameter", "value1").addParameter("parameter", "value2").build();
        assertThat(backendRequest.getURI(), is(newUri));
    }

    @Test
    public void testShouldCreatePostRequestWithParameters() throws Exception {
        String uri = "http://host/path";

        MockHttpServletRequest post = new MockHttpServletRequest("POST", uri);
        post.addParameter("parameter", new String[]{"value1", "value2"});

        HttpEntityEnclosingRequestBase backendRequest = (HttpEntityEnclosingRequestBase) requestFactory.createPassthroughRequest(post);

        assertThat(backendRequest.getMethod(), is("POST"));
        assertThat(backendRequest.getURI(), is(URI.create(uri)));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpEntity entity = backendRequest.getEntity();
        entity.writeTo(baos);
        String body = baos.toString();

        assertThat(body, is("parameter=value1&parameter=value2"));
    }

    @Test
    public void testShouldCreatePostRequestWithRequestBody() throws Exception {
        String uri = "http://host/path";

        MockHttpServletRequest post = new MockHttpServletRequest("POST", uri);
        post.setContent("body".getBytes());

        HttpEntityEnclosingRequestBase backendRequest = (HttpEntityEnclosingRequestBase) requestFactory.createPassthroughRequest(post);

        assertThat(backendRequest.getMethod(), is("POST"));
        assertThat(backendRequest.getURI(), is(URI.create(uri)));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpEntity entity = backendRequest.getEntity();
        entity.writeTo(baos);
        String body = baos.toString();

        assertThat(body, is("body"));
    }
}