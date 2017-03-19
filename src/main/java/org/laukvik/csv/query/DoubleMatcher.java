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
import org.laukvik.csv.columns.DoubleColumn;

import java.util.Arrays;
import java.util.List;

/**
 * Compares a DoubleColumn to a list of values.
 */
public final class DoubleMatcher extends RowMatcher {

    /**
     * The DoubleColumn.
     */
    private final DoubleColumn column;
    /**
     * The list of values.
     */
    private final List<Double> values;

    /**
     * Compares a DoubleColumn to specified doubles.
     *
     * @param doubleColumn the doubleColumn
     * @param doubles      the values
     */
    public DoubleMatcher(final DoubleColumn doubleColumn, final Double... doubles) {
        this(doubleColumn, Arrays.asList(doubles));
    }

    /**
     * Compares a DoubleColumn to specified doubles.
     *
     * @param doubleColumn the doubleColumn
     * @param doubles      the values
     */
    public DoubleMatcher(final DoubleColumn doubleColumn, final List<Double> doubles) {
        this.column = doubleColumn;
        this.values = doubles;
    }

    /**
     * Returns true when the row matches.
     *
     * @param row the row
     * @return true when the row matches
     */
    public boolean matches(final Row row) {
        Double d = row.getDouble(column);
        for (Double v : values) {
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
