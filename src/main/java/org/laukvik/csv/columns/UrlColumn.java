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
        }
        catch (MalformedURLException ex) {
            return null;
        }
    }

    /**
     * Compares two URLs.
     *
     * @param one     one column
     * @param another another column
     * @return
     */
    public int compare(final URL one, final URL another) {
        return one.toExternalForm().compareTo(another.toExternalForm());
    }

    /**
     * Returns the URL as string.
     * @return the string
     */
    public String toString() {
        return getName() + "(URL)";
    }

    /**
     * Returns the hashCode.
     * @return the hashCode
     */
    public int hashCode() {
        return 3;
    }

    /**
     * Returns true if equals obj.
     *
     * @param obj the object to compare with
     * @return true if equals
     */
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UrlColumn other = (UrlColumn) obj;
        return true;
    }

}
