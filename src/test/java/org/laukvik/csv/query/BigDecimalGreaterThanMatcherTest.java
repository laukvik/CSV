package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.columns.BigDecimalColumn;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BigDecimalGreaterThanMatcherTest {

    @Test
    public void getColumn() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("value");
        BigDecimalGreaterThanMatcher m = new BigDecimalGreaterThanMatcher(c, BigDecimal.valueOf(100));
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("value");
        BigDecimalGreaterThanMatcher m = new BigDecimalGreaterThanMatcher(c, BigDecimal.valueOf(100));
        assertTrue(m.matches(BigDecimal.valueOf(101)));
        assertFalse(m.matches(BigDecimal.valueOf(99)));
        assertFalse(m.matches(null));
    }

}