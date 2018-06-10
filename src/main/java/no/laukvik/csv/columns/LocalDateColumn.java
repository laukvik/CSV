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

import java.time.LocalDate;

/**
 * Column with LocalDate as the data type.
 */
public final class LocalDateColumn extends Column<LocalDate> {

    /**
     * Creates a new column with the columnName.
     *
     * @param columnName the column name
     */
    public LocalDateColumn(final String columnName) {
        super(columnName);
    }

    @Override
    public String asString(LocalDate value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public LocalDate parse(String value) {
        return value == null || value.equals("") ? null : LocalDate.parse(value);
    }

    @Override
    public int compare(LocalDate one, LocalDate another) {
        return one.compareTo(another);
    }


}
