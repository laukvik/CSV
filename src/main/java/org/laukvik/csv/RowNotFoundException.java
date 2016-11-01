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
 * Indicates that the row could not be found.
 */
public final class RowNotFoundException extends IllegalArgumentException {

    /**
     * The row index.
     */
    private final int rowIndex;

    /**
     * The row with the index could not be found.
     *
     * @param index the index
     */
    public RowNotFoundException(final int index) {
        super("Row with index " + index + " was not found.");
        this.rowIndex = index;
    }

    /**
     * Returns the row index that could not be found.
     *
     * @return the row index
     */
    public int getRowIndex() {
        return rowIndex;
    }
}
