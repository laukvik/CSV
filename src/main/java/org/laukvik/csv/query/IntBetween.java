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
import org.laukvik.csv.columns.IntegerColumn;


public class IntBetween extends RowMatcher {

    int min;
    int max;
    private final IntegerColumn column;

    public IntBetween(IntegerColumn column, int min, int max) {
        super();
        this.column = column;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean mathes(Row row) {
        Integer value = row.getInteger(column);
        if (value == null) {
            return false;
        }
        return value >= min && value <= max;
    }

}
