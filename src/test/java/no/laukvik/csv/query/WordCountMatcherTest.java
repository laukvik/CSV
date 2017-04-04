package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.StringColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WordCountMatcherTest {

    @Test
    public void getColumn() {
        StringColumn c = new StringColumn("value");
        WordCountMatcher m = new WordCountMatcher(c, 11);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn c = csv.addStringColumn("value");
        WordCountMatcher m = new WordCountMatcher(c, 3);
        assertFalse(m.matches(""));
        assertFalse(m.matches(null));
        assertTrue(m.matches(" The red fox."));
        assertFalse(m.matches("The red fox lazy dog. "));
        assertFalse(m.matches("   The red fox     dog    "));
        assertTrue(m.matches("The red dog."));
        assertTrue(m.matches("The lazy dog."));
    }

}