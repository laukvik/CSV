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
import org.laukvik.csv.columns.UrlColumn;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Compares a UrlColumn to have the specified value.
 */
public final class UrlFilePrefixMatcher implements ValueMatcher<URL> {

    /**
     * The value to compare.
     */
    private final List<String> values;
    /**
     * The column to compare.
     */
    private final UrlColumn column;

    /**
     * Matches the prefix of the file in the URL.
     *
     * @param urlColumn the column
     * @param value     the value
     */
    public UrlFilePrefixMatcher(final UrlColumn urlColumn, final String... value) {
        this(urlColumn, Arrays.asList(value));
    }

    /**
     * Matches the prefix of the file in the URL.
     * @param urlColumn the column
     * @param values the list of values
     */
    public UrlFilePrefixMatcher(final UrlColumn urlColumn, final List<String> values) {
        this.column = urlColumn;
        this.values = values;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(URL value) {
        return values.contains(UrlColumn.getPrefix(value));
    }
}
