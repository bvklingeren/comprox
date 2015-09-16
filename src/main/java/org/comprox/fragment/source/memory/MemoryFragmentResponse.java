package org.comprox.fragment.source.memory;

import org.comprox.fragment.FragmentResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *
 */
public class MemoryFragmentResponse implements FragmentResponse {

    private final byte[] bytes;

    public MemoryFragmentResponse(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }
}
