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
package org.laukvik.csv.query;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;

import java.util.Arrays;
import java.util.List;

/**
 * Compares a IntegerColumn to be equal to an odd value.
 */
public final class ParityMatcher extends RowMatcher {

    public static final String ODD = "Odd";
    public static final String EVEN = "Even";

    /**
     * The value to match.
     */
    private final List<Boolean> values;
    /**
     * The column to match.
     */
    private final IntegerColumn column;

    /**
     * The value of the column must be equal to the value.
     *
     * @param integerColumn the column
     * @param value         the value
     */
    public ParityMatcher(final IntegerColumn integerColumn, final Boolean... value) {
        super();
        this.column = integerColumn;
        this.values = Arrays.asList(value);
    }

    public ParityMatcher(final IntegerColumn integerColumn, final List<Boolean> values) {
        super();
        this.column = integerColumn;
        this.values = values;
    }

    /**
     * Returns true when the row matches.
     *
     * @param row the row
     * @return true when the row matches
     */
    public boolean matches(final Row row) {
        Integer i = row.getInteger(column);
        return IntegerColumn.isOdd(i);
    }

}
