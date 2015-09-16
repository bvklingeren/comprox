package org.comprox.fragment.source.http;

import org.comprox.fragment.FragmentResponse;

import java.io.InputStream;

/**
 *
 */
public class HttpFragmentResponse implements FragmentResponse {

    private final InputStream inputStream;

    public HttpFragmentResponse(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }
}
