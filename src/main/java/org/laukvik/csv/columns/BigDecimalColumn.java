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

import java.math.BigDecimal;

/**
 * Column with BigDecimal as the data type.
 */
public final class BigDecimalColumn extends Column<BigDecimal> {

    /**
     * Column with BigDecimal as the data type.
     *
     * @param name the name of the column
     */
    public BigDecimalColumn(final String name) {
        super(name);
    }

    /**
     * Returns the value as a String.
     *
     * @param value the value
     * @return the String representation
     */
    public String asString(final BigDecimal value) {
        return value.toString();
    }

    /**
     * Parses the string.
     *
     * @param value the string
     * @return BigDecimal
     */
    public BigDecimal parse(final String value) {
        return new BigDecimal(value);
    }

    /**
     * Compares two BigDecimals.
     *
     * @param one     one column
     * @param another another column
     * @return the compare value
     */
    public int compare(final BigDecimal one, final BigDecimal another) {
        return one.compareTo(another);
    }

    /**
     * Returns the String representation of the column.
     *
     * @return thre String representation
     */
    public String toString() {
        return getName() + "(Integer)";
    }

    /**
     * Returns the hash code.
     *
     * @return the hash code
     */
    public int hashCode() {
        return 7;
    }

    /**
     * Returns true if the objects are equals.
     *
     * @param obj another BigDecimal column
     * @return true when equals
     */
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BigDecimalColumn other = (BigDecimalColumn) obj;
        return true;
    }

}
