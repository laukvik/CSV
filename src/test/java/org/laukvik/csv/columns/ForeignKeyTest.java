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

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class ForeignKeyTest {

    @Test
    public void validParseFormat() {
        ForeignKey.parse("table[column]");
    }

    @Test(expected = IllegalForeignKeyException.class)
    public void missingStartParantheses() {
        ForeignKey.parse("table column]");
    }

    @Test(expected = IllegalForeignKeyException.class)
    public void missingColumn() {
        ForeignKey.parse("table");
    }

    @Test(expected = IllegalForeignKeyException.class)
    public void missingTable() {
        ForeignKey.parse("[column]");
    }

    @Test(expected = IllegalForeignKeyException.class)
    public void missingTableAndColumn() {
        ForeignKey.parse("[]");
    }

    @Test(expected = IllegalForeignKeyException.class)
    public void emptyColumn() {
        ForeignKey.parse("table[]");
    }

    @Test(expected = IllegalForeignKeyException.class)
    public void tableAndColumnNull() {
        new ForeignKey(null, null);
    }

    @Test(expected = IllegalForeignKeyException.class)
    public void emptyValueShouldFail2() {
        new ForeignKey("", "");
    }

    @Test(expected = IllegalForeignKeyException.class)
    public void emptyTableAndColumn() {
        new ForeignKey(null, "");
    }

    @Test(expected = IllegalForeignKeyException.class)
    public void emptyTableAndNullColumn() {
        new ForeignKey("", null);
    }

    @Test(expected = IllegalForeignKeyException.class)
    public void emptyValueShouldFail5() {
        new ForeignKey(" ", " ");
    }

}
