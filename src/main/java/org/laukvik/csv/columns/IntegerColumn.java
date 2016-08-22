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


public class IntegerColumn extends Column<Integer> {

    String name;

    public IntegerColumn(String name) {
        this.name = name;
    }

    public IntegerColumn() {
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String asString(Integer value) {
        return value.toString();
    }

    @Override
    public Integer parse(String value) {
        return Integer.parseInt(value);
    }

    public int compare(Integer one, Integer another) {
        return one.compareTo(another);
    }

    @Override
    public String toString() {
        return name + "(Integer)";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IntegerColumn other = (IntegerColumn) obj;
        return true;
    }

}
