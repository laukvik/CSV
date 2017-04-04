package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.StringColumn;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class StringInMatcherTest {

    @Test
    public void isAny() throws Exception {
        assertTrue(StringInMatcher.isAny("Bob", "Bob", "Lob", "Job"));
        assertTrue(StringInMatcher.isAny("Lob", "Bob", "Lob", "Job"));
        assertTrue(StringInMatcher.isAny("Job", "Bob", "Lob", "Job"));
        assertFalse(StringInMatcher.isAny("Job"));
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("first");
        StringInMatcher m = new StringInMatcher(first, "Steve", "Bill");
        assertTrue(m.matches("Steve"));
        assertTrue(m.matches("Bill"));
        assertFalse(m.matches("Unkown"));
        assertFalse(m.matches(null));
    }

}