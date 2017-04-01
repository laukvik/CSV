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
import org.laukvik.csv.columns.DoubleColumn;

/**
 * Compares a IntegerColumn to be less than a value.
 */
public final class DoubleLessThanMatcher implements ValueMatcher<Double> {

    /**
     * The value to match.
     */
    private final double value;
    /**
     * The column to match.
     */
    private final DoubleColumn column;

    /**
     * The value of the column must be value.
     *
     * @param doubleColumn the column
     * @param value        the value
     */
    public DoubleLessThanMatcher(final DoubleColumn doubleColumn, final double value) {
        super();
        this.column = doubleColumn;
        this.value = value;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final Double i) {
        return i != null && i < value;
    }
}
