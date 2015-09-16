package org.comprox.parser;

import java.io.Writer;

/**
 *
 */
public class HtmlResponse {

    private final Writer writer;

    public HtmlResponse(Writer writer) {
        this.writer = writer;
    }

    public Writer getWriter() {
        return writer;
    }
}
