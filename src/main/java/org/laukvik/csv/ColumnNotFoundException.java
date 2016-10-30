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
 * Indicates that the column could not be found.
 */
final class ColumnNotFoundException extends IllegalArgumentException {

    /**
     * Indicates that the column with the index cant be found.
     *
     * @param index the column index
     */
    ColumnNotFoundException(final int index) {
        super("Column with index " + index + " was not found");
    }

    /**
     * Indicates that the column could not be found.
     *
     * @param name the column name
     */
    ColumnNotFoundException(final String name) {
        super("Column with name " + name + " was not found");
    }

}
