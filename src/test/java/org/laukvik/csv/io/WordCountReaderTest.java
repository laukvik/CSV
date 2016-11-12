package org.laukvik.csv.io;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

/**
 * @author Morten Laukvik
 */
public class WordCountReaderTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = WordCountReader.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }


    @Test
    public void shouldReadWords() throws FileNotFoundException {
        CSV csv = new CSV();
        WordCountReader reader = new WordCountReader();
        reader.readFile(getResource("words.txt"), csv);
        assertEquals(2, csv.getColumnCount());
        StringColumn wordColumn = (StringColumn) csv.getColumn(0);
        StringColumn countColumn = (StringColumn) csv.getColumn(1);
    }

}