package no.laukvik.csv.statistics;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BigDecimalRangeTest {

    @Test
    public void addValue() throws Exception {
        BigDecimalRange r = new BigDecimalRange("", new BigDecimal(22), new BigDecimal(333));
        r.addValue(new BigDecimal(21));
        r.addValue(new BigDecimal(334));
        assertEquals(0, r.getCount());
        r.addValue(new BigDecimal(22));
        assertEquals(1, r.getCount());
        r.addValue(new BigDecimal(333));
        assertEquals(1, r.getCount());
    }

    @Test
    public void contains() throws Exception {
        BigDecimalRange r = new BigDecimalRange("", new BigDecimal(55), new BigDecimal(99));
        assertTrue(r.contains(new BigDecimal(55)));
        assertFalse(r.contains(new BigDecimal(99)));
        assertFalse(r.contains(new BigDecimal(54)));
        assertFalse(r.contains(new BigDecimal(99.1)));
        assertFalse(r.contains(new BigDecimal(54.9)));
        assertFalse(r.contains(null));
    }

}