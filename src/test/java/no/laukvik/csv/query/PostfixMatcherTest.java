package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.StringColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PostfixMatcherTest {

    @Test
    public void getColumn() {
        StringColumn c = new StringColumn("value");
        PostfixMatcher m = new PostfixMatcher(c, "");
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("filename");
        PostfixMatcher m = new PostfixMatcher(first, "jpg");
        assertTrue(m.matches("thumb-01.jpg"));
        assertTrue(m.matches("thumb-02.jpg"));
        assertFalse(m.matches("thumb-01.gif"));
        assertFalse(m.matches(null));
    }

}