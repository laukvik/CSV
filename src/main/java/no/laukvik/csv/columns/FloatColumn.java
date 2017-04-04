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
package no.laukvik.csv.columns;

/**
 * Column with Float as the data type.
 */
public final class FloatColumn extends Column<Float> {

    /**
     * Column with Float as the data type.
     *
     * @param name the name of the column
     */
    public FloatColumn(final String name) {
        super(name);
    }

    /**
     * Returns the float as a String.
     *
     * @param value the value
     * @return the value as a String
     */
    @Override
    public String asString(final Float value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
     * Returns the float value of a string.
     *
     * @param value the string
     * @return the float value
     */
    @Override
    public Float parse(final String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return Float.parseFloat(value);
    }

    /**
     * Compares two float values.
     *
     * @param one     one column
     * @param another another column
     * @return the comparison
     */
    @Override
    public int compare(final Float one, final Float another) {
        return compareWith(one, another);
    }

}
