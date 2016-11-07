package org.laukvik.csv;

import org.junit.Test;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.IntegerColumn;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

/**
 */
public class MetaDataTest {

    @Test
    public void addColumns() {
        MetaData md = new MetaData();
        md.addColumn("first");
        assertEquals(1, md.getColumnCount());
        md.addColumn("second");
        assertEquals(2, md.getColumnCount());
        md.addColumn(new IntegerColumn("int"));
        assertEquals(3, md.getColumnCount());
    }

    @Test
    public void removeColumn() {
        MetaData md = new MetaData();
        Column c = md.addColumn("first");
        assertEquals(1, md.getColumnCount());
        md.removeColumn(c);
        assertEquals(0, md.getColumnCount());
    }

    @Test
    public void getColumn() {
        MetaData md = new MetaData();
        Column c1 = md.addColumn("first");
        Column c2 = md.addColumn("second");
        assertEquals(c1, md.getColumn("first"));
        assertEquals(c2, md.getColumn(1));
    }

    @Test
    public void indexOf() {
        MetaData md = new MetaData();
        Column c1 = md.addColumn("James");
        Column c2 = md.addColumn("Bond");
        assertEquals(0, md.indexOf(c1));
        assertEquals(1, md.indexOf(c2));
    }

    @Test
    public void moveColumn() {
        MetaData md = new MetaData();
        Column c1 = md.addColumn("one");
        Column c2 = md.addColumn("two");
        Column c3 = md.addColumn("three");
        Column c4 = md.addColumn("four");
        md.moveColumn(0, 3);
        assertEquals(0, md.indexOf(c2));
        assertEquals(1, md.indexOf(c3));
        assertEquals(2, md.indexOf(c4));
        assertEquals(3, md.indexOf(c1)); // two three four one
        md.moveColumn(3, 0);
        assertEquals(0, md.indexOf(c1));
        assertEquals(1, md.indexOf(c2));
        assertEquals(2, md.indexOf(c3));
        assertEquals(3, md.indexOf(c4));
        md.moveColumn(1, 2);
        assertEquals(0, md.indexOf(c1));
        assertEquals(1, md.indexOf(c3));
        assertEquals(2, md.indexOf(c2));
        assertEquals(3, md.indexOf(c4));
    }

    @Test
    public void swapColumn() {
        MetaData md = new MetaData();
        Column c1 = md.addColumn("one");
        Column c2 = md.addColumn("two");
        Column c3 = md.addColumn("three");
        md.swapColumn(0, 2);
        assertEquals(2, md.indexOf(c1));
        assertEquals(1, md.indexOf(c2));
        assertEquals(0, md.indexOf(c3));
    }

    @Test
    public void properties() {
        MetaData md = new MetaData();
        md.setCharset(Charset.forName("utf-8"));
        md.setQuoteChar(CSV.DOUBLE_QUOTE);
        md.setSeparator(CSV.COMMA);
        md.setCSV(null);

        assertEquals("UTF-8", md.getCharset().name());
        assertEquals(CSV.DOUBLE_QUOTE, md.getQuoteChar());
        assertEquals(CSV.COMMA, md.getSeparatorChar());

    }

}