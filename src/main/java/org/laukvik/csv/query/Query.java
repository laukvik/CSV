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
package org.laukvik.csv.query;

import org.laukvik.csv.CSV;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.DoubleColumn;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * The query contains the criteria to be used when filtering the data set.
 *
 */
public class Query {

    private final MetaData metaData;
    private final CSV csv;
    private Where where;
    private Select select;

    public Query(CSV csv) {
        this.metaData = csv.getMetaData();
        this.csv = csv;
        this.select = new Select();
        this.select.where = this.where();
    }

    public Select select() {
        select = new Select();
        select.where = where;
        return select;
    }

    public Select select(org.laukvik.csv.columns.Column... columns) {
        select.columns = columns;
        return select;
    }

    public Where where() {
        where = new Where();
        where.query = this;
        return where;
    }

    public OrderBy orderBy() {
        return this.where().orderBy();
    }

    public List<Row> getResultList() {
        List<Row> filteredRows = new ArrayList<>();
        int matchesRequired = where.columns.size();
        for (int rowIndex = 0; rowIndex < csv.getRowCount(); rowIndex++) {

            Row r = csv.getRow(rowIndex);
            if (matchesRequired == 0) {
                /* Dont use filters - add all */
                filteredRows.add(r);
            } else {
                /* Use filtering */
                int matchCount = 0;
                for (Column c : where.columns) {
                    if (c.matcher.matches(r)) {
                        matchCount++;
                    }
                }
                if (matchCount == matchesRequired) {
                    filteredRows.add(r);
                }
            }
        }
        if (!where.orderBy.sortOrders.isEmpty()) {
            Collections.sort(filteredRows, new RowSorter(where.orderBy.sortOrders, metaData));
        }
        return filteredRows;
    }

    /**
     *
     */
    public class Column {

        final org.laukvik.csv.columns.Column col;
        Where where;
        RowMatcher matcher;

        public Column(org.laukvik.csv.columns.Column col) {
            this.col = col;
        }

        public Where isNotEmpty() {
            matcher = new NotEmptyMatcher(col);
            return where;
        }

        public Where isEmpty() {
            matcher = new EmptyMatcher(col);
            return where;
        }

        /* String */
        public Where is(String value) {
            matcher = new StringInMatcher((StringColumn) col, value);
            return where;
        }

        public Where isIn(String... value) {
            matcher = new StringInMatcher((StringColumn) col, value);
            return where;
        }

        /* int */
        public Where is(int value) {
            matcher = new IntIsMatcher((IntegerColumn) col, value);
            return where;
        }

        public Where isBetween(int min, int max) {
            matcher = new IntBetween((IntegerColumn) col, min, max);
            return where;
        }

        public Where isIn(Integer... values) {
            matcher = new IntIsInMatcher((IntegerColumn) col, values);
            return where;
        }

        public Where isIn(Float[] values) {
            matcher = new IsInMatcher<>((FloatColumn) col, values);
            return where;
        }

        public Where isIn(Double[] values) {
            matcher = new IsInMatcher<>((DoubleColumn) col, values);
            return where;
        }

        public Where isGreaterThan(int value) {
            matcher = new IntGreaterThanMatcher((IntegerColumn) col, value);
            return where;
        }

        public Where isLessThan(int value) {
//            if (col instanceof IntegerColumn) {
            matcher = new IntLessThan((IntegerColumn) col, value);
//            }
            return where;
        }

        public Where isDate(Date value) {
            if (col instanceof DateColumn) {
                matcher = new DateIsMatcher((DateColumn) col, value);
            } else {
                throw new IllegalArgumentException("Column " + col + " is not a date column!");
            }
            return where;
        }

        public Where isDateGreaterThan(Date value) {
            if (!(col instanceof DateColumn)) {
                throw new IllegalArgumentException("Column " + col + " is not a date column!");
            }
            matcher = new DateGreaterThan((DateColumn) col, value);
            return where;
        }

        public Where isDateLessThan(Date value) {
            matcher = new DateLessThan((DateColumn) col, value);
            return where;
        }

        public Where isYear(int value) {
            if (col instanceof DateColumn) {
                matcher = new YearIs((DateColumn) col, value);
            } else {
                throw new IllegalArgumentException("Column " + col + " is not dateformat!");
            }
            return where;
        }

        public Where in(String... selection) {
            matcher = new StringInMatcher((StringColumn) col, selection);
            return where;
        }

        public Where isIn(Date[] arr) {
            if (col instanceof DateColumn) {
                DateColumn dc = (DateColumn) col;
                matcher = new DateIsInMatcher(dc, arr);
            } else {
                throw new IllegalArgumentException("Column " + col + " is not dateformat!");
            }
            return where;
        }

    }

    public class Where {

        private final List<Column> columns;
        private final OrderBy orderBy;
        private Query query;

        public Where() {
            columns = new ArrayList<>();
            orderBy = new OrderBy(this);
            query = null;
        }

        public Column column(String columnName) {
            org.laukvik.csv.columns.Column col = csv.getMetaData().getColumn(columnName);
            Column c = new Column(col);
            c.where = this;
            columns.add(c);
            return c;
        }

        public Column column(org.laukvik.csv.columns.Column col) {
            Column c = new Column(col);
            c.where = this;
            columns.add(c);
            return c;
        }

        public OrderBy orderBy() {
            return orderBy;
        }

        public List<Row> getResultList() {
            return query.getResultList();
        }

    }

    public class OrderBy {

        private final Where where;
        private final List<SortOrder> sortOrders;

        public OrderBy(Where where) {
            this.where = where;
            sortOrders = new ArrayList<>();
        }

        public boolean isEmpty() {
            return sortOrders.isEmpty();
        }

        public OrderBy asc(org.laukvik.csv.columns.Column column) {
            sortOrders.add(new SortOrder(column, SortOrder.ASC));
            return this;
        }

        public OrderBy desc(org.laukvik.csv.columns.Column column) {
            sortOrders.add(new SortOrder(column, SortOrder.DESC));
            return this;
        }

        public List<Row> getResultList() {
            return where.query.getResultList();
        }

    }

    public class Select {

        private org.laukvik.csv.columns.Column[] columns;
        private Where where;

        public Select(org.laukvik.csv.columns.Column... columns) {
            this.columns = columns;
        }

        public Where where() {
            return where;
        }
    }

}
