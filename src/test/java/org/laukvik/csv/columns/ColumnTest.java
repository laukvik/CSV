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

import org.junit.Assert;
import org.junit.Test;


public class ColumnTest {

    @Test
    public void defaultValues() {
        IntegerColumn c = (IntegerColumn) Column.parseName("Presidency(type=int,default=1)");
        Assert.assertEquals("defaultValue", "1", c.getDefaultValue());
    }

    @Test
    public void foreignKey() {
        IntegerColumn c = (IntegerColumn) Column.parseName("Presidency(type=int,foreignKey=Employee[id])");
        ForeignKey fk1 = new ForeignKey("Employee", "id");
        ForeignKey fk2 = c.getForeignKey();
        Assert.assertEquals("foreignKey", fk1, fk2);
    }

    @Test
    public void parseInteger() {
        IntegerColumn c = (IntegerColumn) Column.parseName("Presidency(type=INT,primaryKey=true,increment=true,foreignKey=Employee[id])");
        Assert.assertEquals("Presidency", c.getName());
        Assert.assertEquals("primaryKey", true, c.isPrimaryKey());
    }

    @Test
    public void parseDate() {
        DateColumn c = (DateColumn) Column.parseName("Took office(type=Date,format=MM/dd/yyyy)");
        Assert.assertEquals("Took office", c.getName());
        Assert.assertEquals("MM/dd/yyyy", c.getFormat());
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
        Assert.assertEquals("President", c.getName());
        Assert.assertEquals("allowNulls", true, c.isAllowNulls());
        Assert.assertEquals("primaryKey", true, c.isPrimaryKey());
        Assert.assertEquals(20, c.getSize());
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

}
