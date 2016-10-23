package org.laukvik.csv.query;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Morten Laukvik
 */
public class StringInMatcherTest {

    @Test
    public void isAny() throws Exception {
        Assert.assertEquals(true, StringInMatcher.isAny("Bob", "Bob", "Lob", "Job"));
        Assert.assertEquals(true, StringInMatcher.isAny("Lob", "Bob", "Lob", "Job"));
        Assert.assertEquals(true, StringInMatcher.isAny("Job", "Bob", "Lob", "Job"));
    }

}