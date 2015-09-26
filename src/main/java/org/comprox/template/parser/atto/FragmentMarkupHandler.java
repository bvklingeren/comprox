package org.comprox.template.parser.atto;

import org.attoparser.AbstractChainedMarkupHandler;
import org.attoparser.MarkupParser;
import org.attoparser.ParseException;
import org.attoparser.output.OutputMarkupHandler;
import org.comprox.template.parser.FragmentFactory;

import java.io.Writer;

/**
 *
 */
public class FragmentMarkupHandler extends AbstractChainedMarkupHandler {

    private final MarkupParser markupParser;
    private final FragmentFactory fragmentFactory;

    private String fragmentContent;

    public FragmentMarkupHandler(MarkupParser markupParser, FragmentFactory fragmentFactory, Writer writer) {
        super(new OutputMarkupHandler(writer));
        this.markupParser = markupParser;
        this.fragmentFactory = fragmentFactory;
    }

    @Override
    public void handleAttribute(char[] buffer, int nameOffset, int nameLen, int nameLine, int nameCol,
                                int operatorOffset, int operatorLen, int operatorLine, int operatorCol,
                                int valueContentOffset, int valueContentLen, int valueOuterOffset, int valueOuterLen,
                                int valueLine, int valueCol) throws ParseException {
        String attributeName = new String(buffer, nameOffset, nameLen);
        if (attributeName.equals("cp-url")) {
            final String location = new String(buffer, valueContentOffset, valueContentLen);
            fragmentContent = fragmentFactory.newFragment(location);
        } else {
            getNext().handleAttribute(buffer, nameOffset, nameLen, nameLine, nameCol, operatorOffset, operatorLen,
                    operatorLine, operatorCol, valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                    valueLine, valueCol);
        }
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
