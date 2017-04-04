package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.DateColumn;
import org.junit.Test;

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
        Date from = created.parse("01.01.1900");
        Date to = created.parse("01.01.2000");
        DateBetweenMatcher m = new DateBetweenMatcher(created, from, to);

        assertTrue(m.matches(created.parse("01.01.1950")));
        assertTrue(m.matches(created.parse("01.01.2000")));
    }

}