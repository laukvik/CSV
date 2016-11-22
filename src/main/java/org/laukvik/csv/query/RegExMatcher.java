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

import java.util.regex.Pattern;

/**
 * Compares a StringColumn to have one or more of the specified values.
 */
public final class RegExMatcher extends RowMatcher {

    /** The Column to match. */
    private final StringColumn column;

    /** The compiled regular expression pattern. */
    private final Pattern pattern;

    /**
     * The value of the column must be among the values.
     *
     * @param stringColumn the column
     * @param regex       the value
     */
    public RegExMatcher(final StringColumn stringColumn, final String regex) {
        super();
        this.column = stringColumn;
        this.pattern = Pattern.compile(regex);
    }

    /**
     * Returns true when the row matches.
     *
     * @param row the row
     * @return true when the row matches
     */
    public boolean matches(final Row row) {
        String val = row.getString(column);
        return pattern.matcher(val).matches();
    }

}
