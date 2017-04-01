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
 * Column with Double as the data type.
 */
public final class DoubleColumn extends Column<Double> {

    /**
     * Column with Double as the data type.
     *
     * @param name the name of the column
     */
    public DoubleColumn(final String name) {
        super(name);
    }

    /**
     * Returns the value as a String.
     *
     * @param value the value
     * @return the String
     */
    @Override
    public String asString(final Double value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
     * Returns the double value of the String or null if parsing failed.
     *
     * @param value the string
     * @return the double value
     */
    @Override
    public Double parse(final String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return Double.parseDouble(value);
    }

    /**
     * Compares one and another.
     *
     * @param one     one column
     * @param another another column
     * @return the comparison
     */
    @Override
    public int compare(final Double one, final Double another) {
        return compareWith(one, another);
    }

}
