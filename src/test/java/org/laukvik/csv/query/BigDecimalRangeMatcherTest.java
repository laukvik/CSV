package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.BigDecimalColumn;
import org.laukvik.csv.statistics.BigDecimalRange;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BigDecimalRangeMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        BigDecimalColumn col = new BigDecimalColumn("val");
        csv.addColumn(col);
        Row r1 = csv.addRow().setBigDecimal(col, new BigDecimal(100));
        Row r2 = csv.addRow().setBigDecimal(col, new BigDecimal(200));
        Row r3 = csv.addRow().setBigDecimal(col, new BigDecimal(100));
        Row r4 = csv.addRow().setBigDecimal(col, new BigDecimal(300));
        Row r5 = csv.addRow().setBigDecimal(col, null);


        BigDecimalRange range = new BigDecimalRange("Hundred", new BigDecimal(99), new BigDecimal(201));

        BigDecimalRangeMatcher m1 = new BigDecimalRangeMatcher(col, range);
        assertTrue(m1.matches(r1));
        assertTrue(m1.matches(r2));
        assertTrue(m1.matches(r3));
        assertFalse(m1.matches(r4));
        assertFalse(m1.matches(r5));
    }


}