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
import no.laukvik.csv.columns.DoubleColumn;

/**
 * Compares a DoubleColumn to be between to values.
 */
public final class DoubleBetweenMatcher implements ValueMatcher<Double> {

    /**
     * The smallest allowed value.
     */
    private final double min;
    /**
     * The largest allowed value.
     */
    private final double max;
    /**
     * The column.
     */
    private final DoubleColumn column;

    /**
     * The value of the column must be between the minimum and maximum value.
     *
     * @param integerColumn the IntegerColumn
     * @param minimum       the minimum value
     * @param maximum       the maximum value
     */
    public DoubleBetweenMatcher(final DoubleColumn integerColumn, final double minimum, final double maximum) {
        super();
        this.column = integerColumn;
        this.min = minimum;
        this.max = maximum;
    }

    /**
     * Returns true if the value is between minimum and maximum.
     *
     * @param value   the value
     * @param minimum the minimum value
     * @param maximum the maximum value
     * @return when the value is between minimum and maximum
     */
    public static boolean isBetween(final Double value, final double minimum, final double maximum) {
        if (value == null) {
            return false;
        }
        return value >= minimum && value < maximum;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final Double value) {
        return isBetween(value, min, max);
    }
}
