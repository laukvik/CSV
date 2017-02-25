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
import org.laukvik.csv.columns.UrlColumn;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Compares a StringColumn to have the specified value.
 */
public final class UrlFilePrefixMatcher extends RowMatcher {

    /**
     * The value to compare.
     */
    private final List<String> values;
    /** The column to compare. */
    private final UrlColumn column;

    /**
     * The value of the column must be.
     * @param urlColumn the column
     * @param value the value
     */
    public UrlFilePrefixMatcher(final UrlColumn urlColumn, final String... value) {
        super();
        this.column = urlColumn;
        this.values = Arrays.asList(value);
    }

    public UrlFilePrefixMatcher(final UrlColumn urlColumn, final List<String> values) {
        super();
        this.column = urlColumn;
        this.values = values;
    }

    /**
     * Returns true when the row matches.
     *
     * @param row the row
     * @return true when the row matches
     */
    public boolean matches(final Row row) {
        URL v = row.getURL(column);
        if (v == null){
            return false;
        } else {
            String filename = UrlColumn.getPrefix(v);
            if (filename == null){
                return false;
            }
            for (String s : values){
                if (filename.equals(s)){
                    return true;
                }
            }
            return false;
        }
    }


}
