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
 * Compares a IntegerColumn to be less than a value.
 */
public final class IntegerLessThanMatcher implements ValueMatcher<Integer> {

    /**
     * The value to match.
     */
    private final int value;
    /**
     * The column to match.
     */
    private final IntegerColumn column;

    /**
     * The value of the column must be value.
     *
     * @param integerColumn the column
     * @param value         the value
     */
    public IntegerLessThanMatcher(final IntegerColumn integerColumn, final int value) {
        super();
        this.column = integerColumn;
        this.value = value;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final Integer i) {
        return i != null && i < value;
    }
}
