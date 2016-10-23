package org.laukvik.csv.io;

import org.junit.Test;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
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
        WordCountReader reader = new WordCountReader();
        reader.readFile(getResource("words.txt"));
        MetaData metaData = reader.getMetaData();
        assertEquals(2, metaData.getColumnCount());
        StringColumn wordColumn = (StringColumn) metaData.getColumn(0);
        StringColumn countColumn = (StringColumn) metaData.getColumn(1);
        while (reader.hasNext()){
            Row row = reader.next();
        }
    }

}