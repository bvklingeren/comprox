package org.comprox.template.request;

import org.comprox.fragment.FragmentRequest;

/**
 *
 */
public interface ClientRequest {
    FragmentRequest createFragmentRequest(String location);

    String getLocation();
}
