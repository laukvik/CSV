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
public final class SortOrder {

    /**
     * Ascending sort order.
     */
    public static final Type ASC = Type.ASC;
    /**
     * Descending sort order.
     */
    public static final Type DESC = Type.DESC;
    /**
     * Natural sort order.
     */
    public static final Type NONE = Type.NONE;

    /**
     * The column.
     */
    private final Column column;
    /**
     * The order type.
     */
    private final Type type;

    /**
     * Creates a new instance.
     *
     * @param column the column
     * @param type   the type
     */
    public SortOrder(final Column column, final Type type) {
        this.column = column;
        this.type = type;
    }

    /**
     * Returns the column.
     * @return the column
     */
    public Column getColumn() {
        return column;
    }

    /**
     * Returns the order type.
     * @return the order type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Indicates what sort order to use.
     */
    public enum Type {

        /**
         * Ascending sort order.
         */
        ASC,
        /**
         * Descending sort order.
         */
        DESC,
        /**
         * No sort order.
         */
        NONE
    }

}
