package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 */
public class DateBetweenMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DateColumn created = new DateColumn("created", "yyyy.MM.dd");
        csv.addColumn(created);
        Row r1 = csv.addRow().setDate(created, created.parse("01.01.1950") );
        Row r2 = csv.addRow();
        Row r3 = csv.addRow().setDate(created, created.parse("01.01.2000") );
        Row r4 = csv.addRow().setDate(created, created.parse("01.01.1960") );
        Date from = created.parse("01.01.1900");
        Date to = created.parse("01.01.2000");
        DateBetweenMatcher m = new DateBetweenMatcher(created, from, to);

        assertTrue( m.matches(r1) );
        assertTrue( m.matches(r3) );
    }

}