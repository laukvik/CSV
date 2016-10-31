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
 * Indicates that the ForeignKey value could not be parsed.
 */
public final class IllegalForeignKeyException extends RuntimeException {

    /**
     * Contains the illegal foreign key value.
     */
    private final String value;

    /**
     * The value was illegal.
     *
     * @param message the reason why it was illegal
     * @param value   the illegal value
     */
    public IllegalForeignKeyException(final String message, final String value) {
        super(message);
        this.value = value;
    }

    /**
     * Returns the illegal foreign key value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
