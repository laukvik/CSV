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

/**
 */
public class MinTest {

    @Test
    public void aggregateFloat() throws Exception {
        CSV csv = new CSV();
        FloatColumn ic = csv.addFloatColumn("");
        Row r0 = csv.addRow();
        Row r1 = csv.addRow().set(ic, -1f);
        Row r2 = csv.addRow().set(ic, -2f);

        Min min = new Min(ic);
        assertEquals(null, min.getValue());

        min.aggregate(r0);
        assertEquals(null, min.getValue());

        min.aggregate(r1);
        assertEquals(new BigDecimal(-1), min.getValue());

        min.aggregate(r2);
        assertEquals(new BigDecimal(-2), min.getValue());
    }

    @Test
    public void aggregateBigDecimal() throws Exception {
        CSV csv = new CSV();
        BigDecimalColumn ic = csv.addBigDecimalColumn("");
        Row r0 = csv.addRow();
        Row r1 = csv.addRow().set(ic, new BigDecimal(-1));
        Row r2 = csv.addRow().set(ic, new BigDecimal(-2));

        Min min = new Min(ic);
        assertEquals(null, min.getValue());

        min.aggregate(r0);
        assertEquals(null, min.getValue());

        min.aggregate(r1);
        assertEquals(new BigDecimal(-1), min.getValue());

        min.aggregate(r2);
        assertEquals(new BigDecimal(-2), min.getValue());
    }

    @Test
    public void aggregateDouble() throws Exception {
        CSV csv = new CSV();
        DoubleColumn ic = csv.addDoubleColumn("");
        Row r0 = csv.addRow();
        Row r1 = csv.addRow().set(ic, -1d);
        Row r2 = csv.addRow().set(ic, -2d);

        Min min = new Min(ic);
        assertEquals(null, min.getValue());

        min.aggregate(r0);
        assertEquals(null, min.getValue());

        min.aggregate(r1);
        assertEquals(new BigDecimal(-1), min.getValue());

        min.aggregate(r2);
        assertEquals(new BigDecimal(-2), min.getValue());
    }

    @Test
    public void aggregateInteger() throws Exception {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("");
        Row r0 = csv.addRow();
        Row r1 = csv.addRow().set(ic, -1);
        Row r2 = csv.addRow().set(ic, -2);

        Min min = new Min(ic);
        assertEquals(null, min.getValue());

        min.aggregate(r0);
        assertEquals(null, min.getValue());

        min.aggregate(r1);
        assertEquals(new BigDecimal(-1), min.getValue());

        min.aggregate(r2);
        assertEquals(new BigDecimal(-2), min.getValue());
    }

    @Test
    public void string() {
        IntegerColumn ic = new IntegerColumn("int");
        Min min = new Min(ic);
        assertEquals("Min(int)", min.toString());
    }

}