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

import java.time.LocalDateTime;

/**
 * Column with LocalDateTime as the data type.
 */
public final class LocalDateTimeColumn extends Column<LocalDateTime> {

    /**
     * Creates a new column with the columnName.
     *
     * @param columnName the column name
     */
    public LocalDateTimeColumn(final String columnName) {
        super(columnName);
    }

    @Override
    public String asString(LocalDateTime value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public LocalDateTime parse(String value) {
        return value == null || value.equals("") ? null : LocalDateTime.parse(value);
    }

    @Override
    public int compare(LocalDateTime one, LocalDateTime another) {
        return one.compareTo(another);
    }


}
