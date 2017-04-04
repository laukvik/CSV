package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.DateColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MonthMatcherTest {

    @Test
    public void getColumn() {
        DateColumn c = new DateColumn("value");
        MonthMatcher m = new MonthMatcher(c);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DateColumn created = csv.addDateColumn("created", "yyyy.MM.dd");
        MonthMatcher m = new MonthMatcher(created, 0, 1, 2, null);
        assertTrue(m.matches(created.parse("2005.1.1")));
        assertTrue(m.matches(created.parse("2005.2.1")));
        assertTrue(m.matches(created.parse("2005.3.1")));
        assertTrue(m.matches(null));
        assertFalse(m.matches(created.parse("2005.4.1")));
        assertFalse(m.matches(created.parse("2005.5.1")));
    }

}