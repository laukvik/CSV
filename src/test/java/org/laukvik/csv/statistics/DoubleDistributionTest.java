package org.laukvik.csv.statistics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DoubleDistributionTest {

    @Test
    public void buildRange() throws Exception {
        DoubleRange r1 = new DoubleRange("Q1", 0d, 25d);
        DoubleRange r2 = new DoubleRange("Q2", 25d, 50d);
        DoubleRange r3 = new DoubleRange("Q3", 50d, 75d);
        DoubleRange r4 = new DoubleRange("Q4", 75d, 100d);

        DoubleDistribution dist = new DoubleDistribution();
        dist.addRange(r1);
        dist.addRange(r2);
        dist.addRange(r3);
        dist.addRange(r4);

        dist.addValue(0d);
        dist.addValue(24d);
        dist.addValue(25d);
        dist.addValue(44d);
        dist.addValue(55d);
        dist.addValue(110d);

        assertEquals(2, r1.getCount());
        assertEquals(2, r2.getCount());
        assertEquals(1, r3.getCount());
        assertEquals(0, r4.getCount());
    }

}