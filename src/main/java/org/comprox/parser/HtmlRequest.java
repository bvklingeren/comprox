package org.comprox.parser;

import org.comprox.fragment.FragmentRequest;

/**
 *
 */
public class HtmlRequest {

    private final String location;

    public HtmlRequest(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public FragmentRequest createFragmentRequest(String fragmentLocation) {
        return new FragmentRequest(fragmentLocation);
    }
}
