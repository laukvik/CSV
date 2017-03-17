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
import org.laukvik.csv.columns.StringColumn;

import java.util.Arrays;
import java.util.List;

/**
 * Compares a StringColumn to have the specified value.
 */
public final class WordCountMatcher extends RowMatcher {

    /**
     * The value to compare.
     */
    private final List<Integer> values;
    /**
     * The column to compare.
     */
    private final StringColumn column;

    /**
     * The value of the column must be.
     *
     * @param stringColumn the column
     * @param value        the value
     */
    public WordCountMatcher(final StringColumn stringColumn, final Integer... value) {
        super();
        this.column = stringColumn;
        this.values = Arrays.asList(value);
    }

    public WordCountMatcher(final StringColumn stringColumn, final List<Integer> values) {
        super();
        this.column = stringColumn;
        this.values = values;
    }

    /**
     * Returns true when the row matches.
     *
     * @param row the row
     * @return true when the row matches
     */
    public boolean matches(final Row row) {
        String v = row.getString(column);
        if (v == null) {
            return false;
        } else {
            for (int i : values) {
                if (i == StringColumn.getWordCount(v)) {
                    return true;
                }
            }
            return false;
        }
    }

}
