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

import java.time.LocalTime;

/**
 * Column with LocalTime as the data type.
 */
public final class LocalTimeColumn extends Column<LocalTime> {

    /**
     * Creates a new column with the columnName.
     *
     * @param columnName the column name
     */
    public LocalTimeColumn(final String columnName) {
        super(columnName);
    }

    @Override
    public String asString(LocalTime value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public LocalTime parse(String value) {
        return value == null || value.length() == 0 ? null : LocalTime.parse(value);
    }

    @Override
    public int compare(LocalTime one, LocalTime another) {
        return one.compareTo(another);
    }


}
