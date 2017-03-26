package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
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
        BigDecimal r1 = new BigDecimal(100);
        BigDecimal r2 = new BigDecimal(200);
        BigDecimal r3 = new BigDecimal(100);
        BigDecimal r4 = new BigDecimal(300);
        BigDecimal r5 = null;


        BigDecimalRange range = new BigDecimalRange("Hundred", new BigDecimal(99), new BigDecimal(201));

        BigDecimalRangeMatcher m1 = new BigDecimalRangeMatcher(col, range);
        assertTrue(m1.matches(r1));
        assertTrue(m1.matches(r2));
        assertTrue(m1.matches(r3));
        assertFalse(m1.matches(r4));
        assertFalse(m1.matches(r5));

        BigDecimalRangeMatcher m2 = new BigDecimalRangeMatcher(col);
        assertFalse(m2.matches(r1));
        assertFalse(m2.matches(r2));
        assertFalse(m2.matches(r3));
        assertFalse(m2.matches(r4));
        assertFalse(m2.matches(r5));
    }


}