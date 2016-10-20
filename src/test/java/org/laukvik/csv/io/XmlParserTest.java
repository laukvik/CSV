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
        parser.parseFile(getResource("lorem.html"));
    }

}