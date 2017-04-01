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
 * Compares a FloatColumn to be less than a value.
 */
public final class FloatLessThanMatcher implements ValueMatcher<Float> {

    /**
     * The value to match.
     */
    private final float value;
    /**
     * The column to match.
     */
    private final FloatColumn column;

    /**
     * The value of the column must be value.
     *
     * @param floatColumn the column
     * @param value       the value
     */
    public FloatLessThanMatcher(final FloatColumn floatColumn, final float value) {
        super();
        this.column = floatColumn;
        this.value = value;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final Float i) {
        return i != null && i < value;
    }
}
