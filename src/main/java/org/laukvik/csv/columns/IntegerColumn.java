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
 * Column with Integer as the data type.
 */
public final class IntegerColumn extends Column<Integer> {

    /**
     * The value of ten.
     */
    private static final int TEN = 10;
    /**
     * The value of hundred.
     */
    private static final int HUNDRED = 100;
    /**
     * The value of thousand.
     */
    private static final int THOUSAND = 1000;
    /**
     * The value of million.
     */
    private static final int MILLION = 1000000;
    /**
     * The value of billion.
     */
    private static final int BILLION = 1000000000;

    /**
     * Column with Integer as the data type.
     *
     * @param name the name of the column
     */
    public IntegerColumn(final String name) {
        super(name);
    }

    /**
     * Returns true when the value is an odd number.
     *
     * @param value the value
     * @return true when matches
     */
    public static Boolean isOdd(final Integer value) {
        if (value == null) {
            return false;
        } else {
            return (value % 2) != 0;
        }
    }

    /**
     * Returns true when the value is a negative number.
     *
     * @param value the value
     * @return true when matches
     */
    public static boolean isNegative(final Integer value) {
        if (value == null) {
            return false;
        } else {
            return (value < 0);
        }
    }

    /**
     * Returns true when the value is an odd number.
     *
     * @param value the value
     * @param q multiplied with
     * @return true when matches
     */
    public static Integer getMods(final Integer value, final int q) {
        if (value == null) {
            return 0;
        } else {
            return ((value / q) % TEN) * q;
        }
    }

    /**
     * Returns the ten part of the value.
     *
     * @param value the value
     * @return true when matches
     */
    public static Integer getTen(final Integer value) {
        return getMods(value, TEN);
    }

    /**
     * Returns the hundred part of the value.
     *
     * @param value the value
     * @return true when matches
     */
    public static Integer getHundred(final Integer value) {
        return getMods(value, HUNDRED);
    }

    /**
     * Returns the thousand part of the value.
     *
     * @param value the value
     * @return true when matches
     */
    public static Integer getThousand(final Integer value) {
        return getMods(value, THOUSAND);
    }

    /**
     * Returns the million part of the value.
     *
     * @param value the value
     * @return true when matches
     */
    public static Integer getMillion(final Integer value) {
        return getMods(value, MILLION);
    }

    /**
     * Returns the billion part of the value.
     *
     * @param value the value
     * @return true when matches
     */
    public static Integer getBillion(final Integer value) {
        return getMods(value, BILLION);
    }

    /**
     * Returns the value as a String.
     *
     * @param value the value
     * @return the value as a String
     */
    public String asString(final Integer value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
     * Returns the value of the String.
     *
     * @param value the string
     * @return the integer value
     */
    public Integer parse(final String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return Integer.parseInt(value);
    }

    /**
     * Compares the two values.
     *
     * @param one     one column
     * @param another another column
     * @return the compare value
     */
    public int compare(final Integer one, final Integer another) {
        if (one == null || another == null) {
            if (one == null){
                return -1;
            } else {
                return 1;
            }
        }
        return one.compareTo(another);
    }

}
