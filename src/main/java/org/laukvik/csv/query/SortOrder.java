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
 * Sorts a data setRaw using the specified column and sort order.
 */
public final class SortOrder {

    /**
     * Ascending sort order.
     */
    public static final SortDirection ASC = SortDirection.ASC;
    /**
     * Descending sort order.
     */
    public static final SortDirection DESC = SortDirection.DESC;
    /**
     * Natural sort order.
     */
    public static final SortDirection NONE = SortDirection.NONE;

    /**
     * The column.
     */
    private final Column column;
    /**
     * The order type.
     */
    private final SortDirection direction;

    /**
     * Creates a new instance.
     *
     * @param column the column
     * @param direction the direction
     */
    public SortOrder(final Column column, final SortDirection direction) {
        this.column = column;
        this.direction = direction;
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
    public SortDirection getType() {
        return direction;
    }



}
