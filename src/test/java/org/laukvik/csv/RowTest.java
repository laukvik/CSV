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
package org.laukvik.csv;

import org.junit.Test;
import org.laukvik.csv.columns.BooleanColumn;
import org.laukvik.csv.columns.ByteColumn;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class RowTest {

    public RowTest() {
    }

    @Test
    public void shoudAddRow() {
        CSV csv = new CSV();
        Column column = csv.addColumn("column");
        Row r = csv.addRow();
    }

    @Test
    public void shouldSetInteger() {
        CSV csv = new CSV();
        IntegerColumn id = csv.addIntegerColumn(new IntegerColumn("id"));
        Row r = csv.addRow();
        r.update(id, 123);
    }

    @Test
    public void shouldSetFloat() {
        CSV csv = new CSV();
        FloatColumn fc = csv.addFloatColumn(new FloatColumn("id"));
        Row r = csv.addRow();
        r.update(fc, 123.45f);
    }

    @Test
    public void shouldSetString() {
        CSV csv = new CSV();
        StringColumn sc = csv.addStringColumn("desc");
        Row r = csv.addRow();
        r.update(sc, "just testing");
    }

    @Test
    public void shouldSetBoolean() {
        CSV csv = new CSV();
        BooleanColumn bc = csv.addBooleanColumn(new BooleanColumn("isTrue"));
        Row r = csv.addRow();
        r.update(bc, true);
    }

    @Test
    public void shouldSetByte() {
        CSV csv = new CSV();
        ByteColumn bc = csv.addByteColumn(new ByteColumn("byte"));
        Row r = csv.addRow();
        byte a = 2;
        r.update(bc, a);
    }
}
