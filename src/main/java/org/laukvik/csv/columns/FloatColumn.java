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
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class FloatColumn implements Column<Float> {

    String name;

    public FloatColumn(String name) {
        this.name = name;
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
    public String asString(Float value) {
        return value.toString();
    }

    @Override
    public Float parse(String value) {
        return Float.parseFloat(value);
    }

    public int compare(Float one, Float another) {
        return one.compareTo(another);
    }

    @Override
    public String toString() {
        return name + "(Float)";
    }
}
