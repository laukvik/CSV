package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
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
        Row r1 = csv.addRow().setBigDecimal(col, new BigDecimal(100));
        Row r2 = csv.addRow().setBigDecimal(col, new BigDecimal(200));
        Row r3 = csv.addRow().setBigDecimal(col, new BigDecimal(100));
        Row r4 = csv.addRow().setBigDecimal(col, new BigDecimal(300));
        Row r5 = csv.addRow().setBigDecimal(col, null);


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

        BigDecimalMatcher m3 = new BigDecimalMatcher(col);
        assertTrue(m3.matches(r1));
        assertTrue(m3.matches(r2));
        assertTrue(m3.matches(r3));
        assertTrue(m3.matches(r4));
        assertTrue(m3.matches(r5));

        BigDecimalMatcher m4 = new BigDecimalMatcher(col, null, new BigDecimal(100), null);
//        System.out.println("m4: " + m4.getValues().size());
        assertTrue(m4.matches(r1));
        assertFalse(m4.matches(r2));
        assertTrue(m4.matches(r3));
        assertFalse(m4.matches(r4));
        assertTrue(m4.matches(r5));

    }

}