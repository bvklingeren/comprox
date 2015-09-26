package org.comprox.servlet.backend;

import java.io.IOException;
import javax.servlet.ServletException;

/**
 *
 */
public interface Backend {
    void handleRequest() throws ServletException, IOException;
}
