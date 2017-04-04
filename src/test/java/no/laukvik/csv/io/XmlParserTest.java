package no.laukvik.csv.io;

import no.laukvik.csv.io.xml.Attribute;
import no.laukvik.csv.io.xml.Tag;
import no.laukvik.csv.io.xml.XmlListener;
import no.laukvik.csv.io.xml.XmlParseException;
import no.laukvik.csv.io.xml.XmlParser;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * @author Morten Laukvik
 */
public class XmlParserTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = XmlParserTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void parseHtml() throws XmlParseException {
        XmlParser xml = new XmlParser();
        Tag html = xml.parseFile(getResource("lorem.html"));
        assertEquals("html", html.getName());
        assertEquals("no", html.getAttribute("lang").getValue());
        assertEquals("1", html.getAttribute("version").getValue());
        Tag head = html.getChildren().get(0);
        Tag title = head.getChildren().get(0);
        assertEquals("title", title.getName());
        Tag body = html.getChildren().get(1);
        assertEquals("body", body.getName());

//        assertEquals(4, html.getChildren().size());

//        Tag h1 = body.getChildren().getObject(0);
//        assertEquals("h1", h1.getName());
//
//        Tag p = body.getChildren().getObject(1);
//        assertEquals("p", p.getName());
//
//        Tag img = body.getChildren().getObject(2);
//        assertEquals("img", img.getName());
//
//        Tag input = body.getChildren().getObject(3);
//        assertEquals("input", input.getName());

//        Tag text = p.getChildren().getObject(0);
//        assertEquals("The ", text.getText());
    }

    @Test
    public void parseFile() throws XmlParseException {
        XmlParser parser = new XmlParser();
        Xisten l = new Xisten();
        parser.addListener(l);
        Tag root = parser.parseFile(getResource("lorem.html"));
        assertEquals(10, l.tags);
        assertEquals(8, l.atts);
    }

    class Xisten implements XmlListener {

        int tags = 0;
        int atts = 0;

        @Override
        public void foundTag(Tag tag) {
            tags++;
        }

        @Override
        public void foundAttribute(Attribute attribute) {
            atts++;
        }
    }

}