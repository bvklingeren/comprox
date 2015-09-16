package org.comprox.fragment.source;

import org.comprox.fragment.FragmentRequest;
import org.comprox.fragment.FragmentResponse;
import org.comprox.fragment.InvalidFragmentException;

/**
 *
 */
public abstract class FragmentSourceChain implements FragmentSource {

    private final FragmentSource next;

    public FragmentSourceChain(FragmentSource next) {
        this.next = next;
    }

    protected FragmentSource getNext() {
        return next == null ? new TerminatingFragmentSource() : next;
    }

    private class TerminatingFragmentSource implements FragmentSource {
        @Override
        public FragmentResponse fetch(FragmentRequest request) {
            throw new InvalidFragmentException("Fragment not found at " + request.getLocation());
        }
    }
}
