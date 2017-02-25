package org.laukvik.csv.statistics;

import org.junit.Test;
import org.laukvik.csv.columns.StringColumn;

import static org.junit.Assert.assertEquals;

/**
 * @author Morten Laukvik
 */
public class FrequencyDistributionTest {

    @Test
    public void getColumn() throws Exception {
        StringColumn c = new StringColumn("email");
        FrequencyDistribution fd = new FrequencyDistribution(c);
        assertEquals(c, fd.getColumn());
    }

    @Test
    public void getKeys() throws Exception {
        StringColumn c = new StringColumn("first");
        FrequencyDistribution fd = new FrequencyDistribution(c);
        fd.addValue("James");
        fd.addValue("James");
        assertEquals(1, fd.getKeys().size());
        assertEquals(true, fd.getKeys().contains("James"));
    }

    @Test
    public void getCount() throws Exception {
        StringColumn c = new StringColumn("first");
        FrequencyDistribution fd = new FrequencyDistribution(c);
        fd.addValue("James");
        fd.addValue("Bond");
        fd.addValue("Roger");
        fd.addValue("More");
        fd.addValue("James");
        fd.addValue("More");
        assertEquals((Integer) 2, fd.getCount("James"));
        assertEquals((Integer) 2, fd.getCount("More"));
    }

    @Test
    public void addValue() throws Exception {
        StringColumn c = new StringColumn("first");
        FrequencyDistribution fd = new FrequencyDistribution(c);
        fd.addValue("James");
        fd.addValue("James");
        fd.addValue(null);
        fd.addValue("");
        assertEquals((Integer) 2, fd.getCount("James"));
        assertEquals((Integer) 2, fd.getCount(""));
    }

}