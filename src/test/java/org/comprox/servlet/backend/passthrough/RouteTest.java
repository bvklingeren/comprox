package org.comprox.servlet.backend.passthrough;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.net.URI;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class RouteTest {

    @Test
    public void testShouldMatchRequest() throws Exception {
        assertThat(new Route(request -> true, "bla", 0).matches(new MockHttpServletRequest()), is(true));
    }

    @Test
    public void testShouldNotMatchRequest() throws Exception {
        assertThat(new Route(request -> false, "bla", 0).matches(new MockHttpServletRequest()), is(false));
    }

    @Test
    public void testShouldCreateSimpleUriWithDefaultPort() throws Exception {
        final URI uri = new Route(request -> true, "bla", 0).createUri(new MockHttpServletRequest("", "/path"));
        assertThat(uri.getHost(), is("bla"));
        assertThat(uri.getPort(), is(-1));
        assertThat(uri.getPath(), is("/path"));
        assertThat(uri.getQuery(), is(nullValue()));
    }

    @Test
    public void testShouldCreateUriWithConfiguredPort() throws Exception {
        final URI uri = new Route(request -> true, "bla", 8080).createUri(new MockHttpServletRequest("", "/path"));
        assertThat(uri.getPort(), is(8080));
    }

    @Test
    public void testShouldCreateUriWithQueryString() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setQueryString("param1=foo&param2=bar");

        final URI uri = new Route(r -> true, "bla", 0).createUri(request);
        assertThat(uri.getQuery(), is(request.getQueryString()));
    }

    @Test(expected = InvalidRouteException.class)
    public void testShouldFailToCreateUriWithInvalidUri() throws Exception {
        new Route(request -> true, "", 0).createUri(new MockHttpServletRequest());
    }
}