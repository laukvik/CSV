package org.laukvik.csv.statistics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DoubleDistributionTest {

    @Test
    public void addValue(){

    }

    @Test
    public void buildRange() throws Exception {
        DoubleDistribution d = new DoubleDistribution();
        d.buildRange(0.2, 0.5);

        assertEquals(10, d.getRanges().size());

        assertEquals(0d, d.getRanges().get(0).from, 0);
        assertEquals(0.1d, d.getRanges().get(1).from, 0);
        assertEquals(0.2d, d.getRanges().get(2).from, 0);
    }

}