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

import java.util.Objects;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class ForeignKey {

    private String table;
    private String column;

    public ForeignKey(String table, String column) {
        if (table == null || table.trim().isEmpty()) {
            throw new IllegalForeignKeyException("Illegal foreignKey value ");
        }
        this.table = table;
        this.column = column;
    }

    public static ForeignKey parse(String fkValue) {
        int first = fkValue.indexOf("[");
        if (first == -1) {
            throw new IllegalForeignKeyException("Missing column. Illegal foreignKey value " + fkValue);
        } else {
            int last = fkValue.indexOf("]");
            if (last == -1) {
                throw new IllegalForeignKeyException("Illegal foreignKey value " + fkValue);
            }
            String table = fkValue.substring(0, first);
            if (table.trim().isEmpty()) {
                throw new IllegalForeignKeyException("Missing table name in '" + fkValue + "'");
            }
            String column = fkValue.substring(first + 1, last);
            if (column.trim().isEmpty()) {
                throw new IllegalForeignKeyException("Missing column in '" + fkValue + "'");
            }
//            System.out.println("parse: " + fkValue + " t: " + table + " c: " + column);
            return new ForeignKey(table, column);
        }
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.table);
        hash = 47 * hash + Objects.hashCode(this.column);
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
        final ForeignKey other = (ForeignKey) obj;
        if (!Objects.equals(this.table, other.table)) {
            return false;
        }
        if (!Objects.equals(this.column, other.column)) {
            return false;
        }
        return true;
    }

}
