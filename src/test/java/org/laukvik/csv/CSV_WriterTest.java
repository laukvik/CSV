package org.laukvik.csv;

import org.junit.Test;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.io.BOM;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

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
        assertEquals(2, c2.getRowCount());
        assertEquals(2, c2.getColumnCount());
    }

    @Test
    public void shouldWriteBomUtf8() throws IOException {
        File f = File.createTempFile("CsvWriter_bom", ".csv");
        f = new File("/Users/morten/Desktop/utf8.csv");
        CSV csv1 = new CSV();
        csv1.setCharset(BOM.UTF8.getCharset());
        StringColumn first = csv1.addStringColumn("First");
        StringColumn last = csv1.addStringColumn("Last");
        csv1.addRow().set(first, "Bill").set(last, "Gates");
        csv1.addRow().set(first, "Steve").set(last, "Jobs");
        csv1.writeFile(f);

        CSV csv2 = new CSV();
        csv2.readFile(f);
        assertEquals(BOM.UTF8.getCharset(), csv2.getCharset());
        assertEquals(2, csv2.getRowCount());
        assertEquals(2, csv2.getColumnCount());
    }

}
