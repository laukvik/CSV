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
import java.util.Arrays;
import java.util.List;

/**
 * Compares a Column to be in an array of objects.
 *
 * @param <T> the type of object
 */
public final class IsInMatcher<T> implements ValueMatcher<T> {

    /** The column.  */
    private final Column<T> column;
    /** The values. */
    private final List<T> values;

    /**
     * The value of the column must be in the values.
     *
     * @param column the column
     * @param values the values
     */
    public IsInMatcher(final Column<T> column, final T... values) {
        this.column = column;
        this.values = Arrays.asList(values);
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(T value) {
        return values.contains(value);
    }
}
