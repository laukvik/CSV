package org.laukvik.csv;

import org.junit.Test;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.IntegerColumn;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

/**
 * @author Morten Laukvik
 */
public class CSV_ColumnsTest {


    @Test
    public void addColumns() {
        CSV csv = new CSV();
        csv.addColumn("first");
        assertEquals(1, csv.getColumnCount());
        csv.addColumn("second");
        assertEquals(2, csv.getColumnCount());
        csv.addColumn(new IntegerColumn("int"));
        assertEquals(3, csv.getColumnCount());
    }

    @Test
    public void removeColumn() {
        CSV csv = new CSV();
        Column c = csv.addColumn("first");
        assertEquals(1, csv.getColumnCount());
        csv.removeColumn(c);
        assertEquals(0, csv.getColumnCount());
    }

    @Test
    public void getColumn() {
        CSV csv = new CSV();
        Column c1 = csv.addColumn("first");
        Column c2 = csv.addColumn("second");
        assertEquals(c1, csv.getColumn("first"));
        assertEquals(c2, csv.getColumn(1));
    }

    @Test
    public void indexOf() {
        CSV csv = new CSV();
        Column c1 = csv.addColumn("James");
        Column c2 = csv.addColumn("Bond");
        assertEquals(0, csv.indexOf(c1));
        assertEquals(1, csv.indexOf(c2));
    }

    @Test
    public void moveColumn() {
        CSV csv = new CSV();
        Column c1 = csv.addColumn("one");
        Column c2 = csv.addColumn("two");
        Column c3 = csv.addColumn("three");
        Column c4 = csv.addColumn("four");
        csv.moveColumn(0, 3);
        assertEquals(0, csv.indexOf(c2));
        assertEquals(1, csv.indexOf(c3));
        assertEquals(2, csv.indexOf(c4));
        assertEquals(3, csv.indexOf(c1)); // two three four one
        csv.moveColumn(3, 0);
        assertEquals(0, csv.indexOf(c1));
        assertEquals(1, csv.indexOf(c2));
        assertEquals(2, csv.indexOf(c3));
        assertEquals(3, csv.indexOf(c4));
        csv.moveColumn(1, 2);
        assertEquals(0, csv.indexOf(c1));
        assertEquals(1, csv.indexOf(c3));
        assertEquals(2, csv.indexOf(c2));
        assertEquals(3, csv.indexOf(c4));
    }

    @Test
    public void swapColumn() {
        CSV csv = new CSV();
        Column c1 = csv.addColumn("one");
        Column c2 = csv.addColumn("two");
        Column c3 = csv.addColumn("three");
        csv.swapColumn(0, 2);
        assertEquals(2, csv.indexOf(c1));
        assertEquals(1, csv.indexOf(c2));
        assertEquals(0, csv.indexOf(c3));
    }

    @Test
    public void properties() {
        CSV csv = new CSV();
        csv.setCharset(Charset.forName("utf-8"));
        csv.setQuoteChar(CSV.QUOTE_DOUBLE);
        csv.setSeparator(CSV.COMMA);
        assertEquals("UTF-8", csv.getCharset().name());
        assertEquals( (Character) CSV.QUOTE_DOUBLE, csv.getQuoteChar());
//        assertEquals(CSV.COMMA,csv.getSeparatorChar());

    }

}
