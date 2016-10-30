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
    public String asString(final Float value) {
        return value.toString();
    }

    /**
     * Returns the float value of a string.
     *
     * @param value the string
     * @return the float value
     */
    public Float parse(final String value) {
        return Float.parseFloat(value);
    }

    /**
     * Compares two float values.
     *
     * @param one     one column
     * @param another another column
     * @return the comparison
     */
    public int compare(final Float one, final Float another) {
        return one.compareTo(another);
    }

    /**
     * Returns the Column definition.
     *
     * TODO - Move this into the abstract class
     *
     * @return the column definition
     */
    public String toString() {
        return getName() + "(Float)";
    }

    /**
     * Return the HashCode.
     * @return the HashCode
     */
    public int hashCode() {
        return 7;
    }

    /**
     * Returns true when equals to obj.
     *
     * @param obj the object to compare with
     * @return true when equals
     */
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FloatColumn other = (FloatColumn) obj;
        return true;
    }

}
