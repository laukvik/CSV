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

import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.FloatColumn;

/**
 * Compares a IntegerColumn to be between to values.
 */
public final class FloatBetweenMatcher implements ValueMatcher<Float> {

    /**
     * The smallest allowed value.
     */
    private final float min;
    /**
     * The largest allowed value.
     */
    private final float max;
    /**
     * The column.
     */
    private final FloatColumn column;

    /**
     * The value of the column must be between the minimum and maximum value.
     *
     * @param floatColumn the floatColumn
     * @param minimum     the minimum value
     * @param maximum     the maximum value
     */
    public FloatBetweenMatcher(final FloatColumn floatColumn, final float minimum, final float maximum) {
        super();
        this.column = floatColumn;
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
    public static boolean isBetween(final Float value, final float minimum, final float maximum) {
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
    public boolean matches(final Float value) {
        return isBetween(value, min, max);
    }
}
