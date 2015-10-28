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
        csv.addColumn("column");
        Row r = csv.createRow();
    }

    @Test
    public void shouldSetInteger() {
        CSV csv = new CSV();
        csv.addColumn("id");
        Row r = csv.createRow();
        r.setValue(0, 123);
    }

    @Test
    public void shouldSetFloat() {
        CSV csv = new CSV();
        csv.addColumn("id");
        Row r = csv.createRow();
        r.setValue(0, 123.45);
    }

    @Test
    public void shouldSetString() {
        CSV csv = new CSV();
        csv.addColumn("desc");
        Row r = csv.createRow();
        r.setValue(0, "just testing");
    }

    @Test
    public void shouldSetBoolean() {
        CSV csv = new CSV();
        csv.addColumn("isTrue");
        Row r = csv.createRow();
        r.setValue(0, true);
    }

    @Test
    public void shouldSetByte() {
        CSV csv = new CSV();
        csv.addColumn("byte");
        Row r = csv.createRow();
        byte a = 2;
        r.setValue(0, a);
    }
}
