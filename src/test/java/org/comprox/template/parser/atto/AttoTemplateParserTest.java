package org.comprox.template.parser.atto;

import org.comprox.template.parser.FragmentFactory;
import org.comprox.template.parser.TemplateParser;
import org.comprox.template.source.classpath.ClasspathTemplateSource;
import org.junit.Test;

import java.io.Reader;
import java.io.StringWriter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AttoTemplateParserTest {

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
        TemplateParser parser = new AttoTemplateParser();
        final StringWriter writer = new StringWriter();
        parser.parse(readTemplate(location), new FragmentFactoryStub(), writer);
        assertThat(writer.toString(), is(result));
    }

    private Reader readTemplate(String location) {
        return new ClasspathTemplateSource().getTemplate(location);
    }

    private class FragmentFactoryStub implements FragmentFactory {
        @Override
        public String newFragment(String location) {
            return location.equals("test-url") ? "<div>test</div>" : null;
        }
    }
}