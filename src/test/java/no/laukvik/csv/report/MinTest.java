package no.laukvik.csv.report;

import no.laukvik.csv.columns.IntegerColumn;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 */
public class MinTest {

    @Test
    public void aggregate() throws Exception {
        IntegerColumn c = new IntegerColumn("presidency");
        Min m = new Min(c);
//        m.doSUM(null);
        assertNull(m.getValue());
//        assertEquals((Integer)5, m.getValue());
    }

}