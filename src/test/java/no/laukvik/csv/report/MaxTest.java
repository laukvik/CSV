package no.laukvik.csv.report;

import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.BigDecimalColumn;
import no.laukvik.csv.columns.DoubleColumn;
import no.laukvik.csv.columns.FloatColumn;
import no.laukvik.csv.columns.IntegerColumn;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class MaxTest {

    @Test
    public void aggregateDouble() throws Exception {
        CSV csv = new CSV();
        DoubleColumn ic = csv.addDoubleColumn("");
        Row r0 = csv.addRow();
        Row r1 = csv.addRow().set(ic, 2d);
        Row r2 = csv.addRow().set(ic, 4d);

        Max max = new Max(ic);
        assertEquals(null, max.getValue());

        max.aggregate(r0);
        assertEquals(null, max.getValue());

        max.aggregate(r1);
        assertEquals(new BigDecimal(2), max.getValue());

        max.aggregate(r2);
        assertEquals(new BigDecimal(4), max.getValue());
    }

    @Test
    public void aggregateFloat() throws Exception {
        CSV csv = new CSV();
        FloatColumn ic = csv.addFloatColumn("");
        Row r0 = csv.addRow();
        Row r1 = csv.addRow().set(ic, 2f);
        Row r2 = csv.addRow().set(ic, 4f);

        Max max = new Max(ic);
        assertEquals(null, max.getValue());

        max.aggregate(r0);
        assertEquals(null, max.getValue());

        max.aggregate(r1);
        assertEquals(new BigDecimal(2), max.getValue());

        max.aggregate(r2);
        assertEquals(new BigDecimal(4), max.getValue());
    }

    @Test
    public void aggregateInteger() throws Exception {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("");
        Row r0 = csv.addRow();
        Row r1 = csv.addRow().set(ic, 2);
        Row r2 = csv.addRow().set(ic, 4);

        Max max = new Max(ic);
        assertEquals(null, max.getValue());

        max.aggregate(r0);
        assertEquals(null, max.getValue());

        max.aggregate(r1);
        assertEquals(new BigDecimal(2), max.getValue());

        max.aggregate(r2);
        assertEquals(new BigDecimal(4), max.getValue());
    }

    @Test
    public void aggregateBigDecimal() throws Exception {
        CSV csv = new CSV();
        BigDecimalColumn ic = csv.addBigDecimalColumn("");
        Row r0 = csv.addRow();
        Row r1 = csv.addRow().set(ic, new BigDecimal(2));
        Row r2 = csv.addRow().set(ic, new BigDecimal(4));

        Max max = new Max(ic);
        assertEquals(null, max.getValue());

        max.aggregate(r0);
        assertEquals(null, max.getValue());

        max.aggregate(r1);
        assertEquals(new BigDecimal(2), max.getValue());

        max.aggregate(r2);
        assertEquals(new BigDecimal(4), max.getValue());
    }

    @Test
    public void string() {
        IntegerColumn ic = new IntegerColumn("int");
        Max max = new Max(ic);
        assertEquals("Max(int)", max.toString());
    }

}