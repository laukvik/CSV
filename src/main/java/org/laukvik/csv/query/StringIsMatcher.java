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


public class StringIsMatcher extends RowMatcher {

    private final String[] value;
    private final StringColumn column;

    public StringIsMatcher(StringColumn column, String... value) {
        super();
        this.column = column;
        this.value = value;
    }

    @Override
    public boolean mathes(Row row) {
        String val = row.getString(column);
        if (val == null) {
            return false;
        }
        for (String v : value) {
            if (val.equalsIgnoreCase(v)) {
                return true;
            }
        }
        return false;
    }

}
