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
 * Column with Double as the data type.
 */
public final class DoubleColumn extends Column<Double> {

    /**
     * Column with Double as the data type.
     *
     * @param name the name of the column
     */
    public DoubleColumn(String name) {
        super(name);
    }

    public String asString(Double value) {
        return value.toString();
    }

    public Double parse(String value) {
        return Double.parseDouble(value);
    }

    public int compare(Double one, Double another) {
        return one.compareTo(another);
    }

    public String toString() {
        return getName() + "(Double)";
    }

    public int hashCode() {
        return 7;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DoubleColumn other = (DoubleColumn) obj;
        return true;
    }

}
