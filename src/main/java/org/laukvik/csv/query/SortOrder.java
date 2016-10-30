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

/**
 * Sorts a data set using the specified column and sort order.
 */
public class SortOrder {

    public final static Type ASC = Type.ASC;
    public final static Type DESC = Type.DESC;
    public final static Type NONE = Type.NONE;
    private final Column column;
    private final Type type;

    public SortOrder(Column column, Type type) {
        this.column = column;
        this.type = type;
    }

    public Column getColumn() {
        return column;
    }

    public Type getType() {
        return type;
    }

    public enum Type {

        ASC, DESC, NONE
    }

}
