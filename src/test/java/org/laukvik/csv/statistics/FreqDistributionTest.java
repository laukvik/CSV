package org.laukvik.csv.statistics;

import org.junit.Test;
import org.laukvik.csv.columns.StringColumn;
import static org.junit.Assert.assertEquals;

public class FreqDistributionTest {

    @Test
    public void getColumn() throws Exception {
        StringColumn c = new StringColumn("email");
        FreqDistribution fd = new FreqDistribution(c);
        assertEquals(c, fd.getColumn());
    }

    @Test
    public void getKeys() throws Exception {
        StringColumn c = new StringColumn("first");
        FreqDistribution fd = new FreqDistribution(c);
        fd.addValue("James");
        fd.addValue("James");
        assertEquals(1, fd.getKeys().size());
        assertEquals(true, fd.getKeys().contains("James"));
    }

    @Test
    public void getCount() throws Exception {
        StringColumn c = new StringColumn("first");
        FreqDistribution fd = new FreqDistribution(c);
        fd.addValue("James");
        fd.addValue("Bond");
        fd.addValue("Roger");
        fd.addValue("More");
        fd.addValue("James");
        fd.addValue("More");

        assertEquals( 2, fd.getCount("James"));
        assertEquals( 2, fd.getCount("More"));
    }

    @Test
    public void addValue() throws Exception {
        StringColumn c = new StringColumn("first");
        FreqDistribution fd = new FreqDistribution(c);
        fd.addValue("James");
        fd.addValue("James");
        fd.addValue(null);
        fd.addValue("");
        assertEquals(2, fd.getCount("James"));
        assertEquals(1, fd.getCount(""));
    }

    
}