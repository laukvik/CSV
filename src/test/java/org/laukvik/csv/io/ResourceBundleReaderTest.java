package org.laukvik.csv.io;

import org.junit.Test;
import org.laukvik.csv.CSV;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

/**
 * @author Morten Laukvik
 */
public class ResourceBundleReaderTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = ResourceBundleReaderTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void shouldFindBundles() throws FileNotFoundException {
        ResourceBundleReader r = new ResourceBundleReader();
        File file = getResource("testlang.properties");
        assertEquals(true, file.exists());
        CSV csv = new CSV();
        r.readFile(file, csv);
    }



}