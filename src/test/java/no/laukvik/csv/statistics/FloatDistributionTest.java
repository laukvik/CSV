package no.laukvik.csv.statistics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Morten Laukvik
 */
public class FloatDistributionTest {

    @Test
    public void buildRange() throws Exception {
        FloatRange r1 = new FloatRange("Q1", 0f, 25f);
        FloatRange r2 = new FloatRange("Q2", 25f, 50f);
        FloatRange r3 = new FloatRange("Q3", 50f, 75f);
        FloatRange r4 = new FloatRange("Q4", 75f, 100f);

        FloatDistribution dist = new FloatDistribution();
        dist.addRange(r1);
        dist.addRange(r2);
        dist.addRange(r3);
        dist.addRange(r4);

        dist.addValue(0f);
        dist.addValue(24f);
        dist.addValue(25f);
        dist.addValue(44f);
        dist.addValue(55f);
        dist.addValue(110f);

        assertEquals(2, r1.getCount());
        assertEquals(2, r2.getCount());
        assertEquals(1, r3.getCount());
        assertEquals(0, r4.getCount());

        assertEquals(0, dist.getNullCount());
        dist.addValue(null);
        assertEquals(1, dist.getNullCount());
    }

}