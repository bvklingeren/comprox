package org.comprox.fragment.source.memory;

import org.comprox.fragment.FragmentRequest;
import org.comprox.fragment.FragmentResponse;
import org.comprox.fragment.source.FragmentSource;

import java.util.Map;

/**
 *
 */
public class MemoryFragmentSource implements FragmentSource {

    private final Map<String, String> fragmentMap;

    public MemoryFragmentSource(Map<String, String> fragmentMap) {
        this.fragmentMap = fragmentMap;
    }

    public FragmentResponse fetch(FragmentRequest request) {
        final String fragment = fragmentMap.get(request.getLocation());
        return new MemoryFragmentResponse(fragment.getBytes());
    }
}
