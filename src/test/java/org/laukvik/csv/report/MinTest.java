package org.laukvik.csv.report;

import org.junit.Test;
import org.laukvik.csv.columns.IntegerColumn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 */
public class MinTest {

    @Test
    public void aggregate() throws Exception {
        IntegerColumn c = new IntegerColumn("presidency");
        Min m = new Min(c);
        m.aggregate(null);
        assertNull(m.getValue());
        assertEquals((Integer)5, m.getValue());
    }

}