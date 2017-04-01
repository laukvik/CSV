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
public final class UrlMatcher implements ValueMatcher<URL> {

    /**
     * The value to compare.
     */
    private final List<URL> values;
    /**
     * The column to compare.
     */
    private final UrlColumn column;

    /**
     * Compares a UrlColumn to have the specified value.
     *
     * @param urlColumn the column
     * @param value     the value
     */
    public UrlMatcher(final UrlColumn urlColumn, final URL... value) {
        this(urlColumn, Arrays.asList(value));
    }

    /**
     * Compares a UrlColumn to have the specified value.
     *
     * @param urlColumn the column
     * @param value     the value
     */
    public UrlMatcher(final UrlColumn urlColumn, final List<URL> value) {
        this.column = urlColumn;
        this.values = value;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final URL v) {
        if (v == null) {
            return false;
        } else {
            for (URL s : values) {
                if (v.equals(s)) {
                    return true;
                }
            }
            return false;
        }
    }
}
