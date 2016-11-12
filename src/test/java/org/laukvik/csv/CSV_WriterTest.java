package org.laukvik.csv;

import org.junit.Test;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 */
public class CSV_WriterTest {

    @Test
    public void shouldWrite() throws IOException {
        File f = File.createTempFile("CsvWriter", ".csv");
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");
        csv.addRow().set(first, "Bill").set(last, "Gates");
        csv.addRow().set(first, "Steve").set(last, "Jobs");
        csv.writeFile(f);
        CSV c2 = new CSV();
        c2.readFile(f);
        assertEquals(2, csv.getRowCount());
        assertEquals(2, csv.getColumnCount());
    }

}
