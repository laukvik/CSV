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


public class IntLessThan extends RowMatcher {

    int value;
    IntegerColumn column;

    public IntLessThan(IntegerColumn column, int value) {
        super();
        this.column = column;
        this.value = value;
    }

    @Override
    public boolean mathes(Row row) {
        Integer i = row.getInteger(column);
        if (i == null) {
            return false;
        }
        return i < value;
    }

}
