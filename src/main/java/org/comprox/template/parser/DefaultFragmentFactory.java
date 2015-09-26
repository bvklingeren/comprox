package org.comprox.template.parser;

import org.comprox.fragment.Fragment;
import org.comprox.fragment.FragmentRequest;
import org.comprox.fragment.source.FragmentSource;
import org.comprox.template.request.ClientRequest;

/**
 *
 */
public class DefaultFragmentFactory implements FragmentFactory {

    private final FragmentSource fragmentSource;
    private final ClientRequest request;

    public DefaultFragmentFactory(FragmentSource fragmentSource, ClientRequest request) {
        this.fragmentSource = fragmentSource;
        this.request = request;
    }

    @Override
    public String newFragment(String location) {
        final FragmentRequest fragmentRequest = request.createFragmentRequest(location);
        final Fragment fragment = new Fragment(fragmentSource, fragmentRequest);
        return fragment.render();
    }
}
