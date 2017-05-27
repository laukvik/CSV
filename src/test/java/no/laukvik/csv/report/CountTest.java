package no.laukvik.csv.report;

import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.IntegerColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CountTest {

    @Test
    public void aggregate() throws Exception {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("");
        Row r0 = csv.addRow();
        Row r1 = csv.addRow().set(ic, 2);
        Row r2 = csv.addRow().set(ic, 4);
        Row r3 = csv.addRow().set(ic, 6);

        Count count = new Count(ic);
        assertEquals((Integer) 0, count.getValue());

        count.aggregate(r0);
        assertEquals((Integer) 1, count.getValue());

        count.aggregate(r1);
        assertEquals((Integer) 2, count.getValue());
    }

    @Test
    public void string() {
        IntegerColumn ic = new IntegerColumn("int");
        Count count = new Count(ic);
        assertEquals("Count(int)", count.toString());
    }
}