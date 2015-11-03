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

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class ColumnTest {

    @Test
    public void parseInteger() {
        IntegerColumn c = (IntegerColumn) Column.parseName("Presidency(INT)");
        Assert.assertEquals("Presidency", c.getName());
    }

    @Test
    public void parseDate() {
        DateColumn c = (DateColumn) Column.parseName("Took office(Date=MM/dd/yyyy)");
        Assert.assertEquals("Took office", c.getName());
        Assert.assertEquals("MM/dd/yyyy", c.getFormat());
    }

}
