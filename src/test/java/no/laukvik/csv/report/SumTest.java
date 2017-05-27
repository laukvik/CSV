package no.laukvik.csv.report;

import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.IntegerColumn;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class SumTest {

    @Test
    public void aggregate() throws Exception {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("");
        Row r0 = csv.addRow();
        Row r1 = csv.addRow().set(ic, 100);
        Row r2 = csv.addRow().set(ic, 234);

        Sum sum = new Sum(ic);
        assertEquals(new BigDecimal(0), sum.getValue());

        sum.aggregate(r0);
        assertEquals(new BigDecimal(0), sum.getValue());

        sum.aggregate(r1);
        assertEquals(new BigDecimal(100), sum.getValue());

        sum.aggregate(r2);
        assertEquals(new BigDecimal(334), sum.getValue());
    }

    @Test
    public void getValue() throws Exception {
    }

    @Test
    public void string() throws Exception {
        Sum sum = new Sum(new IntegerColumn("int"));
        assertEquals("Sum(int)", sum.toString());
    }

}