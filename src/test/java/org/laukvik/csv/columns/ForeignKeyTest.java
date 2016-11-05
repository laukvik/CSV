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


public class ForeignKeyTest {

    @Test
    public void shouldTable() {
        ForeignKey fk = new ForeignKey("employee");
        assertEquals("employee", fk.getTable());
    }

    @Test
    public void shouldTableColumn() {
        ForeignKey fk = new ForeignKey("employee", "id");
        assertEquals("employee", fk.getTable());
        assertEquals("id", fk.getColumn());
    }

    @Test
    public void setters() {
        ForeignKey fk = new ForeignKey("employee");
        fk.setTable("customer");
        fk.setColumn("customer_id");
        assertEquals("customer", fk.getTable());
        assertEquals("customer_id", fk.getColumn());
    }

}
