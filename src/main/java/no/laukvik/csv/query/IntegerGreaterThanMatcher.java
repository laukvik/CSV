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
package no.laukvik.csv.query;

import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.IntegerColumn;

/**
 * Compares a IntegerColumn to be greater than a value.
 */
public final class IntegerGreaterThanMatcher implements ValueMatcher<Integer> {

    /**
     * The minimum value.
     */
    private final int min;
    /**
     * The column.
     */
    private final IntegerColumn column;

    /**
     * The value of the column must be greater than minimumValue.
     *
     * @param integerColumn the column
     * @param minimumValue  the minimum value
     */
    public IntegerGreaterThanMatcher(final IntegerColumn integerColumn, final int minimumValue) {
        super();
        this.column = integerColumn;
        this.min = minimumValue;
    }

    /**
     * Returns true if the value is greater than minimum.
     *
     * @param value   the value
     * @param minimum the minimum value
     * @return when the value is greater than minimum
     */
    public static boolean isGreaterThan(final Integer value, final int minimum) {
        if (value == null) {
            return false;
        }
        return value > minimum;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final Integer value) {
        return isGreaterThan(value, min);
    }
}
