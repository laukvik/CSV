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

import org.laukvik.csv.columns.BooleanColumn;
import org.laukvik.csv.columns.Column;

import java.util.Arrays;
import java.util.List;

/**
 * Compares a BooleanColumn to have the specified boolean value.
 *
 */
public final class BooleanMatcher implements ValueMatcher<Boolean> {

    /**
     * The values.
     */
    private final List<Boolean> values;

    /**
     * The column.
     */
    private final BooleanColumn column;

    /**
     * Compares a BooleanColumn to have the specified boolean value.
     *
     * @param booleanColumn the booleanColumn
     * @param values         the boolean values
     */
    public BooleanMatcher(final BooleanColumn booleanColumn, final Boolean... values) {
        this(booleanColumn, Arrays.asList(values));
    }

    /**
     * Compares a BooleanColumn to have the specified boolean value.
     *
     * @param booleanColumn the booleanColumn
     * @param values         the boolean values
     */
    public BooleanMatcher(final BooleanColumn booleanColumn, final List<Boolean> values) {
        this.column = booleanColumn;
        this.values = values;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final Boolean value) {
        return values.contains(value);
    }
}
