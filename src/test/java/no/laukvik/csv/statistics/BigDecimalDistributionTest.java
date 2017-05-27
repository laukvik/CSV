package no.laukvik.csv.statistics;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class BigDecimalDistributionTest {

    @Test
    public void addRange() {
        BigDecimalDistribution d = new BigDecimalDistribution();
        d.addRange(new BigDecimalRange("Small", new BigDecimal("0.1"), new BigDecimal("0.5")));
        d.addRange(new BigDecimalRange("Large", new BigDecimal("0.5"), new BigDecimal("1.0")));
        assertEquals(2, d.getRanges().size());
        assertEquals(0, d.getNullCount());
        d.addValue(null);
        assertEquals(1, d.getNullCount());
        d.addValue(new BigDecimal("0.2"));
        d.addValue(new BigDecimal("0.6"));
        assertEquals(1, d.getRanges().get(0).getCount());
        assertEquals(1, d.getRanges().get(1).getCount());
    }

}