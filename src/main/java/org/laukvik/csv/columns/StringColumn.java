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
     * Returns the text before the last period in the value.
     *
     * @param value the value
     * @return the text before the last period
     */
    public static String getPrefix(final String value) {
        if (value == null) {
            return null;
        } else {
            int index = value.lastIndexOf('.');
            if (index < 0) {
                return null;
            } else {
                return value.substring(0, index);
            }
        }
    }

    /**
     * Returns the text after the last period in the value.
     *
     * @param value the value
     * @return the text after the last period
     */
    public static String getPostfix(final String value) {
        if (value == null) {
            return null;
        } else {
            int index = value.lastIndexOf('.');
            if (index < 0) {
                return null;
            } else {
                return value.substring(index + 1);
            }
        }
    }

    /**
     * Returns the amount of words in the string.
     *
     * @param value the string
     * @return the amount of words
     */
    public static Integer getWordCount(final String value) {
        if (value == null || value.trim().length() == 0) {
            return 0;
        }
        return value.trim().split("\\s+").length;
    }

    /**
     * Returns the first letter of the value.
     *
     * @param value the value
     * @return the first letter
     */
    public static String getFirstLetter(final String value) {
        if (value == null || value.length() == 0) {
            return null;
        } else {
            return value.substring(0, 1);
        }
    }

    /**
     * Returns the length of the string.
     *
     * @param value the string
     * @return the length
     */
    public static int getLength(final String value) {
        return value == null ? 0 : value.length();
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
     * @param columnSize the new size
     */
    public void setSize(final int columnSize) {
        this.size = columnSize;
    }

    /**
     * Returns the value.
     *
     * @param value the value
     * @return the value
     */
    @Override
    public String asString(final String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    /**
     * Returns the value.
     *
     * @param value the string
     * @return the value
     */
    @Override
    public String parse(final String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    /**
     * Compares the two values.
     *
     * @param one     one column
     * @param another another column
     * @return the compare value
     */
    @Override
    public int compare(final String one, final String another) {
        return compareWith(one, another);
    }

}
