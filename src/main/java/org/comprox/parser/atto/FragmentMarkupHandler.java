package org.comprox.parser.atto;

import org.attoparser.AbstractChainedMarkupHandler;
import org.attoparser.MarkupParser;
import org.attoparser.ParseException;
import org.attoparser.output.OutputMarkupHandler;
import org.comprox.fragment.Fragment;
import org.comprox.fragment.FragmentRequest;
import org.comprox.fragment.source.FragmentSource;
import org.comprox.parser.HtmlRequest;
import org.comprox.parser.HtmlResponse;

/**
 *
 */
public class FragmentMarkupHandler extends AbstractChainedMarkupHandler {

    private final HtmlRequest request;
    private final FragmentSource fragmentSource;
    private final MarkupParser markupParser;

    private String fragmentContent;

    protected FragmentMarkupHandler(HtmlRequest request, HtmlResponse response, FragmentSource fragmentSource, MarkupParser markupParser) {
        super(new OutputMarkupHandler(response.getWriter()));
        this.request = request;
        this.fragmentSource = fragmentSource;
        this.markupParser = markupParser;
    }

    @Override
    public void handleAttribute(char[] buffer, int nameOffset, int nameLen, int nameLine, int nameCol,
                                int operatorOffset, int operatorLen, int operatorLine, int operatorCol,
                                int valueContentOffset, int valueContentLen, int valueOuterOffset, int valueOuterLen,
                                int valueLine, int valueCol) throws ParseException {
        String attributeName = new String(buffer, nameOffset, nameLen);
        if (attributeName.equals("cp-url")) {
            final String location = new String(buffer, valueContentOffset, valueContentLen);
            fragmentContent = retrieveFragmentContent(location);
        } else {
            getNext().handleAttribute(buffer, nameOffset, nameLen, nameLine, nameCol, operatorOffset, operatorLen,
                    operatorLine, operatorCol, valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                    valueLine, valueCol);
        }
    }

    private String retrieveFragmentContent(String location) {
        final FragmentRequest fragmentRequest = request.createFragmentRequest(location);
        final Fragment fragment = new Fragment(fragmentSource, fragmentRequest);
        return fragment.render();
    }

    /**
     * Removes whitespace before directives.
     */
    @Override
    public void handleInnerWhiteSpace(char[] buffer, int offset, int len, int line, int col) throws ParseException {
        final String possibleDirectiveStart = new String(buffer, offset + len, 2);
        if (!possibleDirectiveStart.equals("cp")) {
            getNext().handleInnerWhiteSpace(buffer, offset, len, line, col);
        }
    }

    @Override
    public void handleCloseElementStart(char[] buffer, int nameOffset, int nameLen, int line, int col) throws ParseException {
        if (this.fragmentContent != null) {
            markupParser.parse(this.fragmentContent, getNext());
            this.fragmentContent = null;
        }
        getNext().handleCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }
}
