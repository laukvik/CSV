package org.laukvik.csv.statistics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RangedDistributionTest {

    @Test
    public void buildRange() throws Exception {
        IntegerRange r1 = new IntegerRange("Q1", 0, 25);
        IntegerRange r2 = new IntegerRange("Q2", 25, 50);
        IntegerRange r3 = new IntegerRange("Q3", 50, 75);
        IntegerRange r4 = new IntegerRange("Q4", 75, 100);

        IntegerDistribution dist = new IntegerDistribution();
        dist.addRange(r1);
        dist.addRange(r2);
        dist.addRange(r3);
        dist.addRange(r4);

        dist.addValue(0);
        dist.addValue(24);
        dist.addValue(25);
        dist.addValue(44);
        dist.addValue(55);
        dist.addValue(110);
        dist.addValue(null);
        dist.addValue(null);

        assertEquals("Q1", r1.getLabel());
        assertEquals("Q2", r2.getLabel());
        assertEquals("Q3", r3.getLabel());
        assertEquals("Q4", r4.getLabel());

        assertEquals(2, r1.getCount());
        assertEquals(2, r2.getCount());
        assertEquals(1, r3.getCount());
        assertEquals(0, r4.getCount());

        assertEquals(2, dist.getNullCount());
    }

}