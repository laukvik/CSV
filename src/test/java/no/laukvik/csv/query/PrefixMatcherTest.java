package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.StringColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrefixMatcherTest {

    @Test
    public void getColumn() {
        StringColumn c = new StringColumn("value");
        PrefixMatcher m = new PrefixMatcher(c, "");
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("jpg");

        String r1 = "thumb-01.jpg";
        String r2 = "thumb-02.jpg";
        String r3 = "thumb-01.gif";
        String r4 = null;

        PrefixMatcher m = new PrefixMatcher(first, "thumb-01");
        assertTrue(m.matches(r1));
        assertFalse(m.matches(r2));
        assertTrue(m.matches(r3));
        assertFalse(m.matches(r4));

        String s2 = null;
        PrefixMatcher m2 = new PrefixMatcher(first, s2);
        assertFalse(m2.matches(r1));
        assertFalse(m2.matches(r2));
        assertFalse(m2.matches(r3));
        assertTrue(m2.matches(r4));
    }


}