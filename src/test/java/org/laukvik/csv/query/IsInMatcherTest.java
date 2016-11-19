package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.FloatColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Morten Laukvik
 */
public class IsInMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        FloatColumn created = csv.addFloatColumn("created");
        FloatColumn updated = csv.addFloatColumn("updated");
        Row row = csv.addRow().setFloat(created, 3f);

        IsInMatcher<Float> matcher = new IsInMatcher<Float>(created, 1f, 2f, 3f);
        assertTrue( matcher.matches(row) );

        IsInMatcher<Float> matcher2 = new IsInMatcher<Float>(updated, 1f, 2f, 3f);
        assertFalse( matcher2.matches(row) );

        IsInMatcher<Float> matcher3 = new IsInMatcher<Float>(created, 200f, 3000f);
        assertFalse( matcher3.matches(row) );

        IsInMatcher<Float> matcher4 = new IsInMatcher<Float>(created);
        assertTrue( matcher4.matches(row) );
    }

}