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
import org.laukvik.csv.columns.IntegerColumn;

import java.util.Arrays;
import java.util.List;

/**
 * Compares a IntegerColumn to be in an array of values.
 */
public final class IntIsInMatcher implements ValueMatcher<Integer> {

    /**
     * The values to compare to.
     */
    private final List<Integer> values;
    /**
     * The column to use.
     */
    private final IntegerColumn column;

    /**
     * The value of the column must be the same as one or more in the values.
     *
     * @param integerColumn the column
     * @param values        the values
     */
    public IntIsInMatcher(final IntegerColumn integerColumn, final Integer... values) {
        this(integerColumn, Arrays.asList(values));
    }

    /**
     * The value of the column must be the same as one or more in the values.
     *
     * @param integerColumn the column
     * @param values        the values
     */
    public IntIsInMatcher(final IntegerColumn integerColumn, final List<Integer> values) {
        super();
        this.column = integerColumn;
        this.values = values;
    }

    @Override
    public Column getColumn() {
        return column;
    }


    @Override
    public boolean matches(final Integer value) {
        for (Integer v : values) {
            if (value == null) {
                if (v == null) {
                    return true;
                }
            } else {
                if (value.equals(v)) {
                    return true;
                }
            }
        }
        return false;
    }
}
