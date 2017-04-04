package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.FloatColumn;
import no.laukvik.csv.statistics.FloatRange;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FloatRangeMatcherTest {

    @Test
    public void getColumn() {
        FloatColumn c = new FloatColumn("value");
        FloatRangeMatcher m = new FloatRangeMatcher(c);
        assertEquals(c, m.getColumn());
    }


    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        FloatColumn c = csv.addFloatColumn("value");

        FloatRange fr = new FloatRange("untitled", 25f, 45f);

        FloatRangeMatcher m = new FloatRangeMatcher(c, fr);
        assertFalse(m.matches(20f));
        assertTrue(m.matches(30f));
        assertTrue(m.matches(40f));
        assertFalse(m.matches(null));
    }

}