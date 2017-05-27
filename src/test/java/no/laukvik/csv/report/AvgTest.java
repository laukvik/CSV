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

public class AvgTest {

    @Test
    public void aggregateDouble() throws Exception {
        CSV csv = new CSV();
        DoubleColumn c = csv.addDoubleColumn("");
        Row r1 = csv.addRow().set(c, 2d);
        Row r2 = csv.addRow().set(c, 4d);
        Row r3 = csv.addRow().set(c, 6d);
        Avg avg = new Avg(c);
        avg.aggregate(r1);
        avg.aggregate(r2);
        avg.aggregate(r3);
        assertEquals(new BigDecimal(4), avg.getValue());
    }

    @Test
    public void aggregateFloat() throws Exception {
        CSV csv = new CSV();
        FloatColumn c = csv.addFloatColumn("");
        Row r1 = csv.addRow().set(c, 2f);
        Row r2 = csv.addRow().set(c, 4f);
        Row r3 = csv.addRow().set(c, 6f);
        Avg avg = new Avg(c);
        avg.aggregate(r1);
        avg.aggregate(r2);
        avg.aggregate(r3);
        assertEquals(new BigDecimal(4), avg.getValue());
    }

    @Test
    public void aggregateBigDecimal() throws Exception {
        CSV csv = new CSV();
        BigDecimalColumn c = csv.addBigDecimalColumn("");
        Row r1 = csv.addRow().set(c, new BigDecimal(2));
        Row r2 = csv.addRow().set(c, new BigDecimal(4));
        Row r3 = csv.addRow().set(c, new BigDecimal(6));
        Avg avg = new Avg(c);
        avg.aggregate(r1);
        avg.aggregate(r2);
        avg.aggregate(r3);
        assertEquals(new BigDecimal(4), avg.getValue());
    }

    @Test
    public void aggregateInteger() throws Exception {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("");
        Row r0 = csv.addRow();
        Row r1 = csv.addRow().set(ic, 2);
        Row r2 = csv.addRow().set(ic, 4);
        Row r3 = csv.addRow().set(ic, 6);


        Avg avg = new Avg(ic);
        assertEquals(new BigDecimal(0), avg.getValue());

        avg.aggregate(r0);
        assertEquals(new BigDecimal(0), avg.getValue());
        assertEquals(1, avg.getCount());

        avg.aggregate(r1);
        assertEquals(new BigDecimal(1), avg.getValue());
        assertEquals(2, avg.getCount());

        avg.aggregate(r2);
        assertEquals(new BigDecimal(2), avg.getValue());
        assertEquals(3, avg.getCount());

        avg.aggregate(r3);
        assertEquals(new BigDecimal(3), avg.getValue());
        assertEquals(4, avg.getCount());
    }


    @Test
    public void string() {
        IntegerColumn ic = new IntegerColumn("int");
        Avg avg = new Avg(ic);
        assertEquals("Avg(int)", avg.toString());
    }


}