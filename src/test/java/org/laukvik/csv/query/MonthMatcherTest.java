package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.DateColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MonthMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DateColumn created = csv.addDateColumn("created", "yyyy.MM.dd");
        MonthMatcher m = new MonthMatcher(created, 0,1,2,null);
        assertTrue(m.matches(created.parse("2005.1.1")));
        assertTrue(m.matches(created.parse("2005.2.1")));
        assertTrue(m.matches(created.parse("2005.3.1")));
        assertTrue(m.matches(null));
        assertFalse(m.matches(created.parse("2005.4.1")));
        assertFalse(m.matches(created.parse("2005.5.1")));
    }

}