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
package org.laukvik.csv.columns;

/**
 * Column with String as the data type.
 */
public final class StringColumn extends Column<String> {

    /**
     * The size of the column.
     */
    private int size;

    /**
     * Column with String as the data type.
     *
     * @param name the name of the column
     */
    public StringColumn(final String name) {
        super(name);
    }

    /**
     * Returns the size.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size.
     *
     * @param size the new size
     */
    public void setSize(final int size) {
        this.size = size;
    }

    /**
     * Returns the value.
     *
     * @param value the value
     * @return the value
     */
    public String asString(final String value) {
        return value;
    }

    /**
     * Returns the value.
     *
     * @param value the string
     * @return the value
     */
    public String parse(final String value) {
        return value;
    }

    /**
     * Compares the two values.
     *
     * @param one     one column
     * @param another another column
     * @return the compare value
     */
    public int compare(final String one, final String another) {
        return one.compareTo(another);
    }

    /**
     * Returns the column definition.
     * @return the column definition
     */
    public String toString() {
        return getName() + "(String)";
    }

    /**
     * Returns the HashCode.
     * @return the HashCode
     */
    public int hashCode() {
        return 7;
    }

}
