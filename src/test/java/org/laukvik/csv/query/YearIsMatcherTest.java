package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class YearIsMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DateColumn created = csv.addDateColumn("created", "yyyy.MM.dd");
        Row r = csv.addRow();
        r.setDate(created, created.parse("2005.12.1"));
        YearIsMatcher m = new YearIsMatcher(created, 2005);
        assertTrue(m.matches(r));
        m = new YearIsMatcher(created, 2004);
        assertFalse(m.matches(r));
        m = new YearIsMatcher(created, 2006);
        assertFalse(m.matches(r));
    }

}