package org.laukvik.csv.io;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

/**
 * @author Morten Laukvik
 */
public class PropertiesReaderTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = PropertiesReaderTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void shouldFindBundles() throws FileNotFoundException {
        PropertiesReader r = new PropertiesReader();
        File file = getResource("testlang.properties");
        assertEquals(true, file.exists());
        r.readFile(file);
    }

    @Test
    public void shouldFileFilter() throws FileNotFoundException {
        PropertiesReader.BundleFilter filter = new PropertiesReader.BundleFilter("messages");
//        assertEquals(false, filter.accept(new File("/User/morten/messages.properties")));
        assertEquals(true, filter.accept(new File("/User/morten/messages_no.properties")));
        assertEquals(true, filter.accept(new File("/User/morten/messages_dk.properties")));
    }

}