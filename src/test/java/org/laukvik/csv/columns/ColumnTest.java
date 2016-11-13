/*
 * Copyright 2015 Laukviks Bedrifter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laukvik.csv.columns;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ColumnTest {

    @Test
    public void defaultValues() {
        IntegerColumn c = (IntegerColumn) Column.parseName("Presidency(type=int,default=1)");
        assertEquals("1", c.getDefaultValue());
    }

    @Test
    public void foreignKey() {
        IntegerColumn c = (IntegerColumn) Column.parseName("Presidency(type=int,foreignKey=Employee[id])");
        ForeignKey fk1 = new ForeignKey("Employee", "id");
        ForeignKey fk2 = c.getForeignKey();
    }

    @Test
    public void parseInteger() {
        IntegerColumn c = (IntegerColumn) Column.parseName("Presidency(type=INT,primaryKey=true,increment=true,foreignKey=Employee[id])");
        assertEquals("Presidency", c.getName());
        assertEquals("primaryKey", true, c.isPrimaryKey());
    }

    @Test
    public void parseDate() {
        DateColumn c = (DateColumn) Column.parseName("Took office(type=Date,format=MM/dd/yyyy)");
        assertEquals("Took office", c.getName());
        assertEquals("MM/dd/yyyy", c.getFormat());
    }

    @Test(expected = IllegalColumnDefinitionException.class)
    public void illegalDateFormat1() {
        Column.parseName("Took office(type=Date,format=MM/dd/i)");
    }

    @Test(expected = IllegalColumnDefinitionException.class)
    public void illegalDateFormat2() {
        Column.parseName("Took office(type=Date,format)");
    }

    @Test
    public void parseString() {
        StringColumn c = (StringColumn) Column.parseName("President(type=VARCHAR[20],allowNulls=true,primaryKey=true)");
        assertEquals("President", c.getName());
        assertEquals("allowNulls", true, c.isAllowNulls());
        assertEquals("primaryKey", true, c.isPrimaryKey());
        assertEquals(20, c.getSize());
    }

    @Test(expected = IllegalColumnDefinitionException.class)
    public void stringSizeShouldFails1() {
        Column.parseName("President(type=VARCHAR[abcde])");
    }

    @Test(expected = IllegalColumnDefinitionException.class)
    public void stringSizeShouldFails2() {
        Column.parseName("President(type=VARCHAR[1abc])");
    }

    @Test(expected = IllegalColumnDefinitionException.class)
    public void stringSizeShouldFails3() {
        Column.parseName("President(type=VARCHAR[abc1])");
    }

    @Test
    public void stringSizeShouldNotFail() {
        Column.parseName("President(type=VARCHAR[ 1 ])");
    }

    @Test
    public void parseBigDecimal() {
        BigDecimalColumn c = (BigDecimalColumn) Column.parseName("President(type=BIGDECIMAL)");
        assertEquals("President", c.getName());
    }

    @Test
    public void parseBoolean() {
        BooleanColumn c = (BooleanColumn) Column.parseName("President(type=boolean)");
        assertEquals("President", c.getName());
    }

    @Test
    public void parseByte() {
        assertTrue(Column.parseName("President(type=byte)") instanceof ByteColumn);
    }

    @Test
    public void parseDateColumn() {
        assertTrue(Column.parseName("President(type=DATE,format=yyyy.mm.dd)") instanceof DateColumn);
    }

    @Test
    public void parseDouble() {
        assertTrue(Column.parseName("President(type=DOUBLE)") instanceof DoubleColumn);
    }

    @Test
    public void parseFloat() {
        assertTrue(Column.parseName("President(type=FLOAT)") instanceof FloatColumn);
    }

    @Test
    public void parseInt() {
        assertTrue(Column.parseName("President(type=INT)") instanceof IntegerColumn);
    }

    @Test
    public void parseVarchar() {
        assertTrue(Column.parseName("President(type=varchar)") instanceof StringColumn);
    }

    @Test
    public void parseURL() {
        assertTrue(Column.parseName("President(type=URL)") instanceof UrlColumn);
    }

    @Test
    public void settersGetters() {
        Column c = Column.parseName("first");
        c.setVisible(true);
        c.setWidth(10);
        c.setAllowNulls(true);
        c.setPrimaryKey(true);
        c.setDefaultValue("b");
        c.setName("First");
        ForeignKey fk = new ForeignKey("emp","id");
        c.setForeignKey(fk);
        assertTrue(c.isVisible());
        assertTrue(c.isAllowNulls());
        assertTrue(c.isPrimaryKey());
        assertEquals(10, c.getWidth());
        assertEquals("First", c.getName());
        assertEquals("b", c.getDefaultValue());
        assertEquals(fk, c.getForeignKey());
    }

    @Test
    public void toCSV() {
        Column c = Column.parseName("first(type=varchar[32])");
        ColumnDefinition cd = c.toColumnDefinition();
        assertEquals("first(type=varchar[32])", c.toCSV() );
    }

    @Test
    public void compareTo() {
        StringColumn a = new StringColumn("a");
        StringColumn b = new StringColumn("b");
        StringColumn c = new StringColumn("c");
        assertEquals(1, b.compareTo(a));
        assertEquals(0, a.compareTo(a));
        assertEquals(-1, a.compareTo(b));
        assertEquals(1, b.compareTo(null));
        assertEquals(-1, b.compareTo(""));
    }

}
