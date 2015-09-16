package org.comprox.fragment.source;

import org.comprox.fragment.FragmentRequest;
import org.comprox.fragment.FragmentResponse;

import java.io.InputStream;

/**
 *
 */
public interface FragmentSource {

    FragmentResponse fetch(FragmentRequest request);
}
