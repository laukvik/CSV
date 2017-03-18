package org.laukvik.csv.columns;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.laukvik.csv.columns.ColumnDefinition.*;

public class ColumnDefinitionTest {


    @Test
    public void parseInvalid() throws Exception {
        ColumnDefinition cd4 = parse("President(");
        assertEquals("President", cd4.getColumnName());
    }

    @Test
    public void parseNameSimple() throws Exception {
        ColumnDefinition cd4 = parse("President");
        assertEquals("President", cd4.getColumnName());
    }

    @Test
    public void parseNameParanthese() throws Exception {
        ColumnDefinition cd2 = parse("President()");
        assertEquals("President", cd2.getColumnName());
        ColumnDefinition cd3 = parse("President( )");
        assertEquals("President", cd3.getColumnName());
    }

    @Test
    public void parseNameWitAttributes() throws Exception {
        ColumnDefinition cd1 = parse("President(type=VARCHAR[20],primaryKey=true,increment=true,foreignKey=table[id])");
        assertEquals("President", cd1.getColumnName());
    }

    @Test
    public void checkAttributeCount() throws Exception {
        ColumnDefinition cd = parse("President(type=VARCHAR[20],primaryKey=true,increment=true,foreignKey=table[id])");
        assertEquals(4, cd.getAttributeCount());
        ColumnDefinition cd2 = parse("President()");
        assertEquals(0, cd2.getAttributeCount());
        ColumnDefinition cd3 = parse("President(   )");
        assertEquals(0, cd3.getAttributeCount());
        ColumnDefinition cd4 = parse("President(type=VARCHAR[20], primaryKey=true, increment=true)");
        assertEquals(3, cd4.getAttributeCount());
    }

    @Test
    public void parseDetails() throws Exception {
        ColumnDefinition cd = parse("President(type=VARCHAR[20],primaryKey=true,increment=true,foreignKey=table[id])");
        assertEquals("VARCHAR", cd.get("type").getValue());
        assertEquals("20", cd.get("type").getOptional());
        assertEquals("true", cd.get("primaryKey").getValue());
        assertEquals("true", cd.get("increment").getValue());
        assertEquals("table", cd.get("foreignKey").getValue());
        assertEquals("id", cd.get("foreignKey").getOptional());
        assertNull(ColumnDefinition.parse(""));
        assertNull(ColumnDefinition.parse(null));

        cd = ColumnDefinition.parse("President(type=VARCHAR[200),");
        assertEquals("VARCHAR", cd.get("type").getValue());
        assertEquals("200", cd.get("type").getOptional());
    }

    @Test
    public void roundTrip() throws Exception {
        ColumnDefinition cd1 = parse("President(type=VARCHAR,primaryKey=true)");
        assertEquals("President", cd1.getColumnName());
        assertEquals(true, cd1.getBoolean("primaryKey"));
        assertEquals("VARCHAR", cd1.get("type").getValue());
        assertEquals(2, cd1.getAttributeCount());
        String compressed = cd1.toCompressed();
        ColumnDefinition cd2 = parse(compressed);
        assertEquals("President", cd2.getColumnName());
        assertEquals(true, cd2.getBoolean("primaryKey"));
        assertEquals("VARCHAR", cd2.get("type").getValue());
        assertEquals(2, cd2.getAttributeCount());
    }

    @Test
    public void remove() throws Exception {
        ColumnDefinition cd = parse("President");
        cd.setAttribute("primaryKey", new ColumnDefinition.Attribute("true"));
        assertEquals(true, cd.getBoolean("pRiMaryKey"));
        cd.removeAttribute("primaryKey");
        assertEquals(0, cd.getAttributeCount());
    }

    @Test
    public void usingBoolean() throws Exception {
        ColumnDefinition cd = parse("President");
        cd.setAttribute("primaryKey", new ColumnDefinition.Attribute("true"));
        assertEquals(true, cd.getBoolean("primarykey"));
        cd.setAttribute("primaryKey", new ColumnDefinition.Attribute("false"));
        assertEquals(false, cd.getBoolean("primarykey"));
        cd.setAttribute("primaryKey", new ColumnDefinition.Attribute(""));
        assertEquals(false, cd.getBoolean("primarykey"));
    }

    @Test
    public void names() throws Exception {
        ColumnDefinition cd = parse("President(type=VARCHAR,primaryKey=true)");
        assertEquals(true, cd.getAttributeNames().contains("type"));
        assertEquals(true, cd.getAttributeNames().contains("primarykey"));
    }

    @Test
    public void setValue() throws Exception {
        ColumnDefinition cd = parse("President");
        cd.setAttribute(" ",  new ColumnDefinition.Attribute("varchar"));
        assertEquals(0, cd.getAttributeCount());
        cd.setAttribute("tYPe", new ColumnDefinition.Attribute("varchar", "15"));
        assertEquals("varchar", cd.get("type").getValue());
        assertEquals("15", cd.get("type").getOptional());
        cd.get("type").setOptional("16");
        assertEquals("16", cd.get("type").getOptional());
        cd.get("type").setValue("int");
        assertEquals("int", cd.get("type").getValue());
    }

    @Test
    public void setAttribute() throws Exception {
        ColumnDefinition cd = parse("President");
        cd.setAttribute(" ", new ColumnDefinition.Attribute(""));
        assertEquals(0, cd.getAttributeCount());

        cd.setAttribute("", new ColumnDefinition.Attribute(""));
        assertEquals(0, cd.getAttributeCount());

        cd.setAttribute("", null);
        assertEquals(0, cd.getAttributeCount());

        cd.setAttribute(null, null);
        assertEquals(0, cd.getAttributeCount());

    }

}
