package org.laukvik.csv.io;

import org.junit.Test;
import org.laukvik.csv.io.xml.Attribute;
import org.laukvik.csv.io.xml.Tag;
import org.laukvik.csv.io.xml.XmlListener;
import org.laukvik.csv.io.xml.XmlParser;

import java.io.File;

/**
 * @author Morten Laukvik
 */
public class XmlParserTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = XmlParserTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void parseFile() throws Exception {
        XmlParser parser = new XmlParser();
        parser.addListener(new XmlListener() {
            @Override
            public void foundTag(final Tag tag) {
            }

            @Override
            public void foundAttribute(final Attribute attribute) {
            }
        });
        Tag root = parser.parseFile(getResource("lorem.html"));
    }

}