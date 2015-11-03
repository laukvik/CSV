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
import org.laukvik.csv.columns.Column;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 * @param <T>
 */
public class IsInMatcher<T> extends RowMatcher {

    T[] values;
    Column<T> column;

    public IsInMatcher(Column<T> column, T[] values) {
        super();
        this.column = column;
        this.values = values;
        if (values == null) {
            throw new IllegalArgumentException("isIn() value cant be null " + values);
        }
    }

    @Override
    public boolean mathes(Row row) {

//        Object o = row.get(column);
//        if (o == null) {
//            return false;
//        }
//        for (T value : values) {
//            if (value == null) {
//            } else if (o.equals(value)) {
//                return true;
//            }
//        }
        return false;
    }

}
