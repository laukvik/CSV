package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.BigDecimalColumn;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BigDecimalMatcherTest {

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

        BigDecimalMatcher m1 = new BigDecimalMatcher(col, new BigDecimal(100));
        assertTrue(m1.matches(r1));
        assertFalse(m1.matches(r2));
        assertTrue(m1.matches(r3));
        assertFalse(m1.matches(r4));
        assertFalse(m1.matches(r5));

        BigDecimalMatcher m2 = new BigDecimalMatcher(col, new BigDecimal(200), new BigDecimal(300));
        assertFalse(m2.matches(r1));
        assertTrue(m2.matches(r2));
        assertFalse(m2.matches(r3));
        assertTrue(m2.matches(r4));
        assertFalse(m2.matches(r5));

        BigDecimal bd = null;
        BigDecimalMatcher m3 = new BigDecimalMatcher(col, bd);
        assertFalse(m3.matches(r1));
        assertFalse(m3.matches(r2));
        assertFalse(m3.matches(r3));
        assertFalse(m3.matches(r4));
        assertTrue(m3.matches(r5));

        BigDecimalMatcher m4 = new BigDecimalMatcher(col, null, new BigDecimal(100), null);
        assertTrue(m4.matches(r1));
        assertFalse(m4.matches(r2));
        assertTrue(m4.matches(r3));
        assertFalse(m4.matches(r4));
        assertTrue(m4.matches(r5));
    }

}