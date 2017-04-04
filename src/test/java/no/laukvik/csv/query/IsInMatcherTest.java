package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.FloatColumn;
import no.laukvik.csv.columns.StringColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IsInMatcherTest {

    @Test
    public void getColumn() {
        Column c = new StringColumn("first");
        IsInMatcher<Object> m = new IsInMatcher<Object>(c);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        FloatColumn created = csv.addFloatColumn("created");
        FloatColumn updated = csv.addFloatColumn("updated");

        IsInMatcher<Float> matcher = new IsInMatcher<Float>(created, 1f, 2f, 3f);
        assertTrue(matcher.matches(3f));

        IsInMatcher<Float> matcher2 = new IsInMatcher<Float>(updated, 1f, 2f, 3f);
        assertFalse(matcher2.matches(4f));

        IsInMatcher<Float> matcher3 = new IsInMatcher<Float>(created, 200f, 3000f);
        assertFalse(matcher3.matches(3f));

        IsInMatcher<Float> matcher4 = new IsInMatcher<Float>(created);
        assertFalse(matcher4.matches(3f));
    }

}