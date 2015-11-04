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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.laukvik.csv.MetaData;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 * @param <T>
 */
public abstract class Column<T> implements Comparable {

    private MetaData metaData;
    private boolean primaryKey;
    private boolean allowNulls;
    private ForeignKey foreignKey;
    private String defaultValue;

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

    public ForeignKey getForeignKey() {
        return foreignKey;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setForeignKey(ForeignKey foreignKey) {
        this.foreignKey = foreignKey;
    }

    public boolean isAllowNulls() {
        return allowNulls;
    }

    public void setAllowNulls(boolean allowNulls) {
        this.allowNulls = allowNulls;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
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

    /**
     * "President(type=VARCHAR,primaryKey=true,increment=true,foreignKey=null)"
     *
     * @param columnNameWithOptionalMetaData
     * @return
     */
    public static Column parseName(String columnNameWithOptionalMetaData) {
        /* Extract extra information about the column*/
        String columnName = null;
        // Variables for meta values
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        // Look for metadata in column headers
        int firstIndex = columnNameWithOptionalMetaData.indexOf("(");
        if (firstIndex == -1) {
            // No extra information
            columnName = columnNameWithOptionalMetaData;
        } else {
            // Found extra information
            int lastIndex = columnNameWithOptionalMetaData.indexOf(")", firstIndex);
            columnName = columnNameWithOptionalMetaData.substring(0, firstIndex);
            if (lastIndex == -1) {
            } else {
                // String with metadata
                String extraDetails = columnNameWithOptionalMetaData.substring(firstIndex + 1, lastIndex);

                String[] keyValues;
                if (extraDetails.contains(",")) {
                    keyValues = extraDetails.split(",");
                } else {
                    keyValues = new String[]{extraDetails};
                }
                // Extract all key/value pairs from metadata
                for (String keyValue : keyValues) {
                    if (keyValue.contains("=")) {
                        String[] arr = keyValue.split("=");
                        String key = arr[0];
                        String value = arr[1];
                        keys.add(key.trim());
                        values.add(value.trim());
                    } else {
                        keys.add(keyValue.trim());
                        values.add("");
                    }
                }

            }
        }

        // Find dataType before continuing
        String dataType = "VARCHAR";
        for (int x = 0; x < keys.size(); x++) {
            String key = keys.get(x);
            String val = values.get(x);
            if (key.equalsIgnoreCase("type")) {
                dataType = val;
            }
        }

        boolean allowsNull = true;
        {
            String allowNullValue = getValue("allowNulls", keys, values);
            if (allowNullValue != null) {
                if (allowNullValue.equalsIgnoreCase("true")) {
                    allowsNull = true;
                } else if (allowNullValue.equalsIgnoreCase("false")) {
                    allowsNull = false;
                } else {
                    throw new IllegalColumnDefinitionException("allowNulls  cant be " + allowNullValue);
                }
            }
        }

        boolean primaryKey = false;
        {
            String pkValue = getValue("primaryKey", keys, values);
            if (pkValue != null) {
                if (pkValue.equalsIgnoreCase("true")) {
                    primaryKey = true;
                } else if (pkValue.equalsIgnoreCase("false")) {
                    primaryKey = false;
                } else {
                    throw new IllegalColumnDefinitionException("primaryKey cant be " + pkValue);
                }
            }
        }

        ForeignKey foreignKey = null;
        {
            String fkValue = getValue("foreignKey", keys, values);
            if (fkValue == null || fkValue.trim().isEmpty()) {

            } else {
                foreignKey = ForeignKey.parse(fkValue);
            }
        }

        String defaultValue = getValue("default", keys, values);

        // Find all key pairs
        String s = dataType.toUpperCase();
        if (s.equalsIgnoreCase("INT")) {
            IntegerColumn c = new IntegerColumn(columnName);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.equalsIgnoreCase("FLOAT")) {
            FloatColumn c = new FloatColumn(columnName);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.equalsIgnoreCase("DOUBLE")) {
            DoubleColumn c = new DoubleColumn(columnName);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.equalsIgnoreCase("URL")) {
            UrlColumn c = new UrlColumn(columnName);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.equalsIgnoreCase("BOOLEAN")) {
            BooleanColumn c = new BooleanColumn(columnName);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.equalsIgnoreCase("DATE")) {
            String format = getValue("format", keys, values);
            if (format == null || format.trim().isEmpty()) {
                throw new IllegalColumnDefinitionException("Format cant be empty!");
            }
            try {
                SimpleDateFormat f = new SimpleDateFormat(format);
            }
            catch (Exception e) {
                throw new IllegalColumnDefinitionException("Format cant be " + format + "!");
            }
            DateColumn c = new DateColumn(columnName, format);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.equalsIgnoreCase("BIGDECIMAL")) {
            BigDecimalColumn c = new BigDecimalColumn(columnName);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.startsWith("VARCHAR")) {
            StringColumn c = new StringColumn(columnName);
            c.setAllowNulls(allowsNull);
            c.setPrimaryKey(primaryKey);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            int first = s.indexOf("[");
            if (first != -1) {
                int last = s.lastIndexOf("]");
                String size = s.substring(first + 1, last).trim();
                try {
                    Integer i = Integer.parseInt(size);
                    c.setSize(i);
                }
                catch (Exception e) {
                    throw new IllegalColumnDefinitionException("Column " + columnName + " has invalid size '" + size + "'");
                }
            }
            return c;
        }
        return new StringColumn(columnName);
    }

    private static String getValue(String key, List<String> keys, List<String> values) {
        for (int x = 0; x < keys.size(); x++) {
            String k = keys.get(x);
            if (k.equalsIgnoreCase(key)) {
                return values.get(x);
            }
        }
        return null;
    }

}
