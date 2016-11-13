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

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Column with URL as the data type.
 */
public final class UrlColumn extends Column<URL> {

    /**
     * Column with URL as the data type.
     *
     * @param name the name of the column
     */
    public UrlColumn(final String name) {
        super(name);
    }

    /**
     * Returns the value as String.
     *
     * @param value the value
     * @return the value as String
     */
    public String asString(final URL value) {
        if (value == null) {
            return "";
        }
        return value.toExternalForm();
    }

    /**
     * Parses the value and returns an URL.
     *
     * @param value the string
     * @return null if empty or if URL could not be parsed
     */
    public URL parse(final String value) {
        try {
            return new URL(value);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    /**
     * Compares two URLs.
     *
     * @param one     one column
     * @param another another column
     * @return a comparison value
     */
    public int compare(final URL one, final URL another) {
        if (one == null && another == null) {
            return 0;
        } else if (one == null && another != null) {
            return -1;
        } else if (one != null && another == null) {
            return 1;
        }
        return one.toExternalForm().compareTo(another.toExternalForm());
    }

}
