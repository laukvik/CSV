package org.laukvik.csv.columns;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author Morten Laukvik
 */
public class BigDecimalColumnTest {

    @Test
    public void init() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("column");
        Assert.assertEquals("column", c.getName());
    }

    @Test
    public void parse() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("column");
        Assert.assertEquals(new BigDecimal("153"), c.parse("153"));
    }

    @Test
    public void asString() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("column");
        Assert.assertEquals("153", c.asString(new BigDecimal("153")));
    }

    @Test
    public void compare() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("column");
        Assert.assertEquals(-1, c.compare(new BigDecimal("153"), new BigDecimal("200")));
        Assert.assertEquals(0, c.compare(new BigDecimal("200"), new BigDecimal("200")));
        Assert.assertEquals(1, c.compare(new BigDecimal("200"), new BigDecimal("153")));
    }

    @Test
    public void toHtml() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("column");
        Assert.assertEquals("column(Integer)", c.toHtml());
    }

}