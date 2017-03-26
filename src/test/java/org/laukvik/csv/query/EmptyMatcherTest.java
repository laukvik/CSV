package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class EmptyMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("first");
        NotEmptyMatcher m = new NotEmptyMatcher(first);
        assertTrue(m.matches("Steve"));
        assertFalse(m.matches(null));
    }

}