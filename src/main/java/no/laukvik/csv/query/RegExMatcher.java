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
import no.laukvik.csv.columns.StringColumn;

import java.util.regex.Pattern;

/**
 * Compares a StringColumn to have one or more of the specified values.
 */
public final class RegExMatcher implements ValueMatcher<String> {

    /**
     * The Column to match.
     */
    private final StringColumn column;

    /**
     * The compiled regular expression pattern.
     */
    private final Pattern pattern;

    /**
     * The value of the column must be among the values.
     *
     * @param stringColumn the column
     * @param pattern      the value
     */
    public RegExMatcher(final StringColumn stringColumn, final Pattern pattern) {
        this.column = stringColumn;
        this.pattern = pattern;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final String value) {
        return pattern.matcher(value).matches();
    }
}
