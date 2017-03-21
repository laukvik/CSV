package org.laukvik.csv.statistics;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;
import static org.laukvik.csv.statistics.RangedDistribution.findPlace;

public class RangedDistributionTest {

    @Test
    public void findPlaces(){
        assertEquals(1,  findPlace(new BigDecimal("001")));
        assertEquals(1,  findPlace(new BigDecimal("1.0")));
        assertEquals(1,  findPlace(new BigDecimal("1.000000")));
        assertEquals(2,  findPlace(new BigDecimal("10.00")));
        assertEquals(3,  findPlace(new BigDecimal("100.00")));
        assertEquals(-1, findPlace(new BigDecimal("0.1")));
        assertEquals(-2, findPlace(new BigDecimal("0.01")));
        assertEquals(-2, findPlace(new BigDecimal("000.0120")));
    }

}