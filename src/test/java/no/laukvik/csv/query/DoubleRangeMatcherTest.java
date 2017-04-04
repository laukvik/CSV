package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.DoubleColumn;
import no.laukvik.csv.statistics.DoubleRange;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DoubleRangeMatcherTest {

    @Test
    public void getColumn() {
        DoubleColumn c = new DoubleColumn("value");
        DoubleRangeMatcher m = new DoubleRangeMatcher(c);
        assertEquals(c, m.getColumn());
    }


    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DoubleColumn c = csv.addDoubleColumn("value");
        DoubleRange dr1 = new DoubleRange("", 0d, 250d);
        DoubleRangeMatcher m = new DoubleRangeMatcher(c, dr1);
        assertTrue(m.matches(100d));
        assertTrue(m.matches(200d));
        assertFalse(m.matches(300d));
        assertFalse(m.matches(null));
    }

}