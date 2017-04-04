package no.laukvik.csv.columns;

import no.laukvik.csv.io.CsvReaderTest;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ByteColumnTest {

    final static String mime = "Man is distinguished, not only by his reason, but by this singular passion from other animals, which is a lust of the mind, that by a perseverance of delight in the continued and indefatigable generation of knowledge, exceeds the short vehemence of any carnal pleasure.";

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvReaderTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    public static String getString(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        in.read(buffer);
        return new String(buffer);
    }

    @Test
    public void asString() throws Exception {
        ByteColumn c = new ByteColumn("bytes");
        String value = getString(getResource("base64.txt"));
        byte[] bytes = c.parse(value);
        assertEquals(value.replaceAll("\\W", ""), c.asString(bytes).replaceAll("\\W", ""));
    }

    @Test
    public void parse() throws Exception {
        ByteColumn c = new ByteColumn("bytes");
        String value = getString(getResource("base64.txt"));
        byte[] bytes = c.parse(value);
        assertEquals(mime, new String(bytes));
    }

    @Test
    public void compare() {
        ByteColumn c = new ByteColumn("a");
        byte[] bytes3 = {1, 2, 3};
        byte[] bytes5 = {1, 2, 3, 4, 5};
        assertEquals(-1, c.compare(bytes3, bytes5));
        assertEquals(0, c.compare(bytes3, bytes3));
        assertEquals(1, c.compare(bytes5, bytes3));

        assertEquals(-1, c.compare(null, bytes5));
        assertEquals(0, c.compare(null, null));
        assertEquals(1, c.compare(bytes5, null));
    }

    @Test
    public void compareTo() throws Exception {
        ByteColumn a = new ByteColumn("a");
        ByteColumn b = new ByteColumn("b");
        assertEquals(-1, a.compareTo(b));
        assertEquals(1, b.compareTo(a));
        assertEquals(0, a.compareTo(a));
        assertEquals(1, a.compareTo(null));
    }

}