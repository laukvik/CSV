package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WordCountMatcherTest {

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