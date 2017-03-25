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
import org.laukvik.csv.columns.BooleanColumn;

import java.util.Arrays;
import java.util.List;

/**
 *
 *
 */
public final class BooleanMatcher extends RowMatcher {

    /**
     */
    private final List<Boolean> values;

    /**
     * The column.
     */
    private final BooleanColumn column;

    /**
     *
     * @param booleanColumn the booleanColumn
     * @param values         the boolean values
     */
    public BooleanMatcher(final BooleanColumn booleanColumn, final Boolean... values) {
        this(booleanColumn, Arrays.asList(values));
    }

    public BooleanMatcher(final BooleanColumn booleanColumn, final List<Boolean> values) {
        this.column = booleanColumn;
        this.values = values;
    }

    /**
     * Matches the row.
     *
     * @param row the row to compare
     * @return true if it matches
     */
    public boolean matches(final Row row) {
        return values.contains(row.getBoolean(column));
    }

}
