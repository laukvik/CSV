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

import org.laukvik.csv.MetaData;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 * @param <T>
 */
public abstract class Column<T> implements Comparable {

    private MetaData metaData;

    public abstract String asString(T value);

    public abstract T parse(String value);

    public abstract int compare(T one, T another);

    public abstract String getName();

    public abstract void setName(String name);

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public int indexOf() {
        return metaData.indexOf(this);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Column) {
            Column c = (Column) o;
            return getName().compareTo(c.getName());
        }
        return -1;
    }

    public static Column parseName(String columnName) {
        /* Extract extra information about the column*/
        String name = null;
        String extraDetails = null;
        String dataType = null;
        String option = null;

        int firstIndex = columnName.indexOf("(");
        if (firstIndex == -1) {
            // No extra information
            name = columnName;
        } else {
            // Found extra information
            name = columnName;
            int lastIndex = columnName.indexOf(")", firstIndex);
            name = columnName.substring(0, firstIndex);
            if (lastIndex == -1) {
            } else {

                extraDetails = columnName.substring(firstIndex + 1, lastIndex);

                if (extraDetails.indexOf("=") > -1) {
                    String[] arr = extraDetails.split("=");
                    dataType = arr[0];
                    option = arr[1];
                } else {
                    dataType = extraDetails;
                }

            }
        }

        if (dataType == null) {
            return new StringColumn(name);
        } else {
            String s = dataType.toUpperCase();
            if (s.equalsIgnoreCase("INT")) {
                return new IntegerColumn(name);
            } else if (s.equalsIgnoreCase("FLOAT")) {
                return new FloatColumn(name);
            } else if (s.equalsIgnoreCase("DOUBLE")) {
                return new DoubleColumn(name);
            } else if (s.equalsIgnoreCase("URL")) {
                return new UrlColumn(name);
            } else if (s.equalsIgnoreCase("BOOLEAN")) {
                return new BooleanColumn(name);
            } else if (s.equalsIgnoreCase("DATE")) {
                return new DateColumn(name, option);
            } else {
                return new StringColumn(name);
            }
        }
    }

}
