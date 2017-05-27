package no.laukvik.csv.report;

import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.StringColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NameTest {

    final StringColumn sc = new StringColumn("first");

    @Test
    public void aggregate() throws Exception {
        CSV csv = new CSV();
        csv.addStringColumn("tall");
        Row row = csv.addRow();
        Name name = new Name(sc);
        name.aggregate(row);
        assertEquals("first", name.getValue());
    }

    @Test
    public void getValue() throws Exception {
        Name name = new Name(sc);
        assertEquals("first", name.getValue());
    }

    @Test
    public void string() throws Exception {
        Name name = new Name(sc);
        assertEquals("Name(first)", name.toString());
    }

}