package org.comprox.fragment;

import org.apache.commons.io.IOUtils;
import org.comprox.fragment.source.FragmentSource;

import java.io.IOException;

/**
 *
 */
public class Fragment {

    private final FragmentRequest request;
    private final FragmentSource source;

    public Fragment(FragmentSource source, FragmentRequest request) {
        this.request = request;
        this.source = source;
    }

    public String render() {
        try {
            FragmentResponse response = source.fetch(request);
            return IOUtils.toString(response.getInputStream());
        } catch (IOException e) {
            throw new InvalidFragmentException("Failed to read fragment contents from " + request.getLocation(), e);
        }
    }
}
