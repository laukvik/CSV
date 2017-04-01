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

import org.laukvik.csv.columns.BigDecimalColumn;
import org.laukvik.csv.columns.Column;

import java.math.BigDecimal;

/**
 * Compares a BigDecimalColumn to be less than a value.
 */
public final class BigDecimalGreaterThanMatcher implements ValueMatcher<BigDecimal> {

    /**
     * The value to match.
     */
    private final BigDecimal value;
    /**
     * The column to match.
     */
    private final BigDecimalColumn column;

    /**
     * The value of the column must be value.
     *
     * @param bigDecimalColumn the column
     * @param value            the value
     */
    public BigDecimalGreaterThanMatcher(final BigDecimalColumn bigDecimalColumn, final BigDecimal value) {
        super();
        this.column = bigDecimalColumn;
        this.value = value;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final BigDecimal i) {
        return i != null && value.compareTo(i) < 0;
    }
}
