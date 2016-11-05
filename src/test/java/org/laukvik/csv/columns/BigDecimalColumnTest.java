package org.laukvik.csv.columns;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class BigDecimalColumnTest {

    public static BigDecimalColumn createColumn() {
        BigDecimalColumn c = new BigDecimalColumn("bigDecimalColumn");
        c.setWidth(15);
        c.setForeignKey(new ForeignKey("customer", "customer_id"));
        c.setDefaultValue("32");
        c.setPrimaryKey(true);
        c.setAllowNulls(false);
        return c;
    }

    @Test
    public void init() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("column");
        assertEquals("column", c.getName());
    }

    @Test
    public void parse() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("column");
        assertEquals(new BigDecimal("153"), c.parse("153"));
    }

    @Test
    public void asString() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("column");
        assertEquals("153", c.asString(new BigDecimal("153")));
    }

    @Test
    public void compare() throws Exception {
        BigDecimalColumn c = new BigDecimalColumn("column");
        assertEquals(-1, c.compare(new BigDecimal("153"), new BigDecimal("200")));
        assertEquals(0, c.compare(new BigDecimal("200"), new BigDecimal("200")));
        assertEquals(1, c.compare(new BigDecimal("200"), new BigDecimal("153")));
    }

    @Test
    public void toColumnDefinition() throws Exception {
        BigDecimalColumn c = createColumn();
        ColumnDefinition cd = c.toColumnDefinition();
        assertEquals("bigDecimalColumn", c.getName());
        assertEquals("15", cd.get("width").getValue());
        assertEquals("customer", cd.get("foreignKey").getValue());
        assertEquals("customer_id", cd.get("foreignKey").getOptional());
        assertEquals("true", cd.get("primaryKey").getValue());
        assertEquals("false", cd.get("allowNulls").getValue());
        assertEquals("32", cd.get("default").getValue());
    }

    @Test
    public void toCompressed() throws Exception {
        BigDecimalColumn c = createColumn();
        String csv = c.toCSV();
//        assertEquals("column(Integer)", csv);
    }

}