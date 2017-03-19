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
import org.laukvik.csv.columns.FloatColumn;

import java.util.Arrays;
import java.util.List;

/**
 * Compares a FloatColumn to a list of values.
 */
public final class FloatMatcher extends RowMatcher {

    /**
     * The FloatColumn.
     */
    private final FloatColumn column;
    /**
     * The list of values.
     */
    private final List<Float> values;

    /**
     * The value of the floatColumn must be in the collection of floats.
     *
     * @param floatColumn the floatColumn
     * @param floats      the values
     */
    public FloatMatcher(final FloatColumn floatColumn, final Float... floats) {
        this(floatColumn, Arrays.asList(floats));
    }

    /**
     * The value of the floatColumn must be in the collection of floats.
     *
     * @param floatColumn the floatColumn
     * @param floats      the values
     */
    public FloatMatcher(final FloatColumn floatColumn, final List<Float> floats) {
        this.column = floatColumn;
        this.values = floats;
    }

    /**
     * Returns true when the row matches.
     *
     * @param row the row
     * @return true when the row matches
     */
    public boolean matches(final Row row) {
        Float d = row.getFloat(column);
        for (Float v : values) {
            if (d == null) {
                return v == null;
            } else {
                if (d.equals(v)) {
                    return true;
                }
            }
        }
        return false;
    }

}
