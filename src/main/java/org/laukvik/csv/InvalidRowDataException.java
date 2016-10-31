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
package org.laukvik.csv;

/**
 * Indicates that the row does not have the required amount of columns.
 */
public final class InvalidRowDataException extends ParseException {

    /**
     * The invalid row.
     */
    private final Row invalidRow;

    /**
     * Indicates that the row does not have the required amount of columns.
     *
     * @param columns  the amount of columns
     * @param required the required amount of columns
     * @param rowIndex the index for the row
     * @param row      the row
     */
    public InvalidRowDataException(final int columns, final int required, final int rowIndex, final Row row) {
        super("Invalid columns " + required + " found " + columns + " at row " + rowIndex);
        this.invalidRow = row;
    }

    /**
     * Returns the row.
     *
     * @return the row
     */
    public Row getRow() {
        return invalidRow;
    }

}
