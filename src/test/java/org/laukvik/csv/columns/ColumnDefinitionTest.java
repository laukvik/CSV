package org.laukvik.csv.columns;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColumnDefinitionTest {

    @Test
    public void parseNameSimple() throws Exception {
        ColumnDefinition cd4 = new ColumnDefinition("President");
        assertEquals("President", cd4.getColumnName());
    }

    @Test
    public void parseNameParanthese() throws Exception {
        ColumnDefinition cd2 = new ColumnDefinition("President()");
        assertEquals("President", cd2.getColumnName());
        ColumnDefinition cd3 = new ColumnDefinition("President( )");
        assertEquals("President", cd3.getColumnName());
    }

    @Test
    public void parseNameWitAttributes() throws Exception {
        ColumnDefinition cd1 = new ColumnDefinition("President(type=VARCHAR[20],primaryKey=true,increment=true,foreignKey=table[id])");
        assertEquals("President", cd1.getColumnName());
    }

    @Test
    public void checkAttributeCount() throws Exception {
        ColumnDefinition cd = new ColumnDefinition("President(type=VARCHAR[20],primaryKey=true,increment=true,foreignKey=table[id])");
        assertEquals(4, cd.getAttributeCount());
        ColumnDefinition cd2 = new ColumnDefinition("President()");
        assertEquals(0, cd2.getAttributeCount());
        ColumnDefinition cd3 = new ColumnDefinition("President(   )");
        assertEquals(0, cd3.getAttributeCount());
        ColumnDefinition cd4 = new ColumnDefinition("President(type=VARCHAR[20], primaryKey=true, increment=true)");
        assertEquals(3, cd4.getAttributeCount());
    }

    @Test
    public void parseDetails() throws Exception {
        ColumnDefinition cd = new ColumnDefinition("President(type=VARCHAR[20],primaryKey=true,increment=true,foreignKey=table[id])");
        assertEquals("VARCHAR", cd.get("type").getValue());
        assertEquals("20", cd.get("type").getOptional());
        assertEquals("true", cd.get("primaryKey").getValue());
        assertEquals("true", cd.get("increment").getValue());
        assertEquals("table", cd.get("foreignKey").getValue());
        assertEquals("id", cd.get("foreignKey").getOptional());
    }

    @Test
    public void roundTrip() throws Exception {
        ColumnDefinition cd1 = new ColumnDefinition("President(type=VARCHAR,primaryKey=true)");
        assertEquals("President", cd1.getColumnName());
        assertEquals(true, cd1.getBoolean("primaryKey"));
        assertEquals("VARCHAR", cd1.get("type").getValue());
        assertEquals(2, cd1.getAttributeCount());
        String compressed = cd1.toCompressed();
        ColumnDefinition cd2 = new ColumnDefinition(compressed);
        assertEquals("President", cd2.getColumnName());
        assertEquals(true, cd2.getBoolean("primaryKey"));
        assertEquals("VARCHAR", cd2.get("type").getValue());
        assertEquals(2, cd2.getAttributeCount());
    }

    @Test
    public void remove() throws Exception {
        ColumnDefinition cd = new ColumnDefinition("President");
        cd.setAttribute("primaryKey", "true");
        assertEquals(true, cd.getBoolean("pRiMaryKey"));
        cd.removeAttribute("primaryKey");
        assertEquals(0, cd.getAttributeCount());
    }

    @Test
    public void usingBoolean() throws Exception {
        ColumnDefinition cd = new ColumnDefinition("President");
        cd.setAttribute("primaryKey", "true");
        assertEquals(true, cd.getBoolean("primarykey"));
        cd.setAttribute("primaryKey", "false");
        assertEquals(false, cd.getBoolean("primarykey"));
        cd.setAttribute("primaryKey", "");
        assertEquals(false, cd.getBoolean("primarykey"));
    }

    @Test
    public void names() throws Exception {
        ColumnDefinition cd = new ColumnDefinition("President(type=VARCHAR,primaryKey=true)");
        assertEquals(true, cd.getAttributeNames().contains("type"));
        assertEquals(true, cd.getAttributeNames().contains("primarykey"));
    }

}
