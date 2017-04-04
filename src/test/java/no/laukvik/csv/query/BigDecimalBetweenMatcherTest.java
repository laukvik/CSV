package no.laukvik.csv.query;

import no.laukvik.csv.columns.BigDecimalColumn;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BigDecimalBetweenMatcherTest {

    @Test
    public void isBetween() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("value");
        assertTrue(BigDecimalBetweenMatcher.isBetween(BigDecimal.valueOf(150), BigDecimal.valueOf(100), BigDecimal.valueOf(200)));
        assertTrue(BigDecimalBetweenMatcher.isBetween(BigDecimal.valueOf(100), BigDecimal.valueOf(100), BigDecimal.valueOf(200)));
        assertTrue(BigDecimalBetweenMatcher.isBetween(BigDecimal.valueOf(199), BigDecimal.valueOf(100), BigDecimal.valueOf(200)));
        assertFalse(BigDecimalBetweenMatcher.isBetween(BigDecimal.valueOf(99), BigDecimal.valueOf(100), BigDecimal.valueOf(200)));
        assertFalse(BigDecimalBetweenMatcher.isBetween(BigDecimal.valueOf(201), BigDecimal.valueOf(100), BigDecimal.valueOf(200)));
        assertFalse(BigDecimalBetweenMatcher.isBetween(null, BigDecimal.valueOf(100), BigDecimal.valueOf(200)));
    }

    @Test
    public void getColumn() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("value");
        BigDecimalBetweenMatcher m = new BigDecimalBetweenMatcher(c, null, null);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("value");
        BigDecimalBetweenMatcher m = new BigDecimalBetweenMatcher(c, BigDecimal.valueOf(100), BigDecimal.valueOf(200));
        assertTrue(m.matches(BigDecimal.valueOf(100)));
        assertTrue(m.matches(BigDecimal.valueOf(199)));
        assertFalse(m.matches(BigDecimal.valueOf(99)));
        assertFalse(m.matches(BigDecimal.valueOf(200)));
    }

}