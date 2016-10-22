package org.laukvik.csv.io;

import org.junit.Test;

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
        parser.addListener(new XmlParser.XmlListener() {
            @Override
            public void foundTag(final XmlParser.Tag tag) {
                System.out.println("Found.tag: " + tag);
            }

            @Override
            public void foundAttribute(final XmlParser.Attribute attribute) {
                System.out.println("Found.attribute: " + attribute);
            }
        });
        XmlParser.Tag root = parser.parseFile(getResource("lorem.html"));
//        System.out.println(root.toHtml());
    }

}