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

import no.laukvik.csv.columns.BigDecimalColumn;
import no.laukvik.csv.columns.Column;

import java.math.BigDecimal;

/**
 * Compares a IntegerColumn to be between to values.
 */
public final class BigDecimalBetweenMatcher implements ValueMatcher<BigDecimal> {

    /**
     * The smallest allowed value.
     */
    private final BigDecimal min;
    /**
     * The largest allowed value.
     */
    private final BigDecimal max;
    /**
     * The column.
     */
    private final BigDecimalColumn column;

    /**
     * The value of the column must be between the minimum and maximum value.
     *
     * @param integerColumn the IntegerColumn
     * @param minimum       the minimum value
     * @param maximum       the maximum value
     */
    public BigDecimalBetweenMatcher(final BigDecimalColumn integerColumn,
                                    final BigDecimal minimum,
                                    final BigDecimal maximum) {
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
    public static boolean isBetween(final BigDecimal value, final BigDecimal minimum, final BigDecimal maximum) {
        if (value == null) {
            return false;
        }
        return value.compareTo(minimum) > -1 && value.compareTo(maximum) < 0;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final BigDecimal value) {
        return isBetween(value, min, max);
    }
}
