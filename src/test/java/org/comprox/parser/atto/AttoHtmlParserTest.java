package org.comprox.parser.atto;

import org.comprox.parser.HtmlRequest;
import org.comprox.parser.HtmlResponse;
import org.comprox.template.classpath.ClasspathTemplateSource;
import org.comprox.fragment.source.memory.MemoryFragmentSource;
import org.comprox.parser.HtmlParser;
import org.junit.Test;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AttoHtmlParserTest {

    private static final String NO_DIRECTIVES =
            "<html>\n" +
            "<head></head>\n" +
            "<body>\n" +
            "<div><img src=\"image.jpg\"></div>\n" +
            "</body>\n" +
            "</html>";
    private static final String SINGLE_DIRECTIVE =
            "<html>\n" +
            "<head></head>\n" +
            "<body>\n" +
            "<div><div>test</div></div>\n" +
            "</body>\n" +
            "</html>";
    @Test
    public void testShouldCopyExactDocumentWithoutDirectives() throws Exception {
        verifyParseResult("no-directives.html", NO_DIRECTIVES);
    }

    @Test
    public void testShouldHandleUrlDirective() throws Exception {
        verifyParseResult("single-directive.html", SINGLE_DIRECTIVE);
    }

    private void verifyParseResult(String location, String result) {
        HtmlParser parser = new AttoHtmlParser(new ClasspathTemplateSource(), new MemoryFragmentSource(createFragmentMap()));
        final StringWriter writer = new StringWriter();
        parser.parse(new HtmlRequest(location), new HtmlResponse(writer));
        assertThat(writer.toString(), is(result));
    }

    private Map<String, String> createFragmentMap() {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("test-url", "<div>test</div>");
        return map;
    }
}