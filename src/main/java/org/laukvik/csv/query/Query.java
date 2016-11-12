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
 * TODO - Add query examples here.
 *
 * <pre>
 * query.count().where().column("BookID").is(5);
 * </pre>
 *
 * <pre>
 * query.select().where().column("BookID").is(5);
 * </pre>
 *
 * <pre>
 * query.select().orderBy().asc("BookID");
 * </pre>
 *
 * csv.getRows(query);
 *
 */
public final class Query {

    /**
     * The CSV data.
     */
    private final CSV csv;
    /** Contains all the row matchers. */
    private Where where;
    /** Contains all column selections. */
    private Select select;

    /**
     * Builds a new empty query.
     *
     * @param csv the CSV
     */
    public Query(final CSV csv) {
        this.csv = csv;
        this.select = new Select();
        this.select.where = this.where();
    }

    /**
     * Selects all columns.
     *
     * @return the select instance
     */
    public Select select() {
        select = new Select();
        select.where = where;
        return select;
    }

    /**
     * Selects one or more columns.
     *
     * @param columns the columns
     * @return the select instance
     */
    public Select select(final org.laukvik.csv.columns.Column... columns) {
        select.columns = columns;
        return select;
    }

    /**
     * Builds a new Where.
     *
     * @return the new instance
     */
    public Where where() {
        where = new Where();
        where.query = this;
        return where;
    }

    /**
     * Returns the OrderBy controller.
     * @return the OrderBy
     */
    public OrderBy orderBy() {
        return this.where().orderBy();
    }

    /**
     * Returns the list of Rows.
     *
     * @return the rows
     */
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
            Collections.sort(filteredRows, new RowSorter(where.orderBy.sortOrders));
        }
        return filteredRows;
    }

    /**
     * Controls how to map columns with matchers.
     *
     * column().isEmpty();
     *
     */
    public final class Column {

        /**
         * The column to match.
         */
        private final org.laukvik.csv.columns.Column col;
        /**
         * The where controller.
         */
        private Where where;
        /**
         * The RowMatcher to use with this column.
         */
        private RowMatcher matcher;

        /**
         * Builds a new column matcher.
         *
         * @param column the column
         */
        public Column(final org.laukvik.csv.columns.Column column) {
            this.col = column;
        }

        /**
         * The value must not be empty.
         *
         * @return the where
         */
        public Where isNotEmpty() {
            matcher = new NotEmptyMatcher(col);
            return where;
        }

        /**
         * The value must be empty.
         *
         * @return the where
         */
        public Where isEmpty() {
            matcher = new EmptyMatcher(col);
            return where;
        }

        /**
         * The value must be the specified value.
         * @param value the value
         * @return the where
         */
        public Where is(final String value) {
            matcher = new StringInMatcher((StringColumn) col, value);
            return where;
        }

        /**
         * The value must be among the values.
         *
         * @param values the values
         * @return the where
         */
        public Where isIn(final String... values) {
            matcher = new StringInMatcher((StringColumn) col, values);
            return where;
        }

        /**
         * The value must be equal to the value.
         * @param value the value
         * @return the where
         */
        public Where is(final int value) {
            matcher = new IntIsMatcher((IntegerColumn) col, value);
            return where;
        }

        /**
         * The value must be between the minimum and maximum (inclusive).
         * @param min the minimum
         * @param max the minimum
         * @return the where
         */
        public Where isBetween(final int min, final int max) {
            matcher = new IntBetween((IntegerColumn) col, min, max);
            return where;
        }

        /**
         * The value must be one among the values.
         * @param values the values
         * @return the where
         */
        public Where isIn(final Integer... values) {
            matcher = new IntIsInMatcher((IntegerColumn) col, values);
            return where;
        }

        /**
         * The value must be one among the values.
         * @param values the values
         * @return the where
         */
        public Where isIn(final Float... values) {
            matcher = new IsInMatcher<>((FloatColumn) col, values);
            return where;
        }

        /**
         * The value must be one among the values.
         * @param values the values
         * @return the where
         */
        public Where isIn(final Double... values) {
            matcher = new IsInMatcher<>((DoubleColumn) col, values);
            return where;
        }

        /**
         * The value must be greater than the value.
         * @param value the value
         * @return the where
         */
        public Where isGreaterThan(final int value) {
            matcher = new IntGreaterThanMatcher((IntegerColumn) col, value);
            return where;
        }

        /**
         * The value must be less than the value.
         * @param value the value
         * @return the where
         */
        public Where isLessThan(final int value) {
            matcher = new IntLessThan((IntegerColumn) col, value);
            return where;

        }

        /**
         * The date must equal to the value.
         * @param value the value
         * @return the where
         */
        public Where isDate(final Date value) {
            if (col instanceof DateColumn) {
                matcher = new DateIsMatcher((DateColumn) col, value);
            } else {
                throw new IllegalArgumentException("Column " + col + " is not a date column!");
            }
            return where;
        }

        /**
         * The date must be greater than the value.
         * @param value the value
         * @return the where
         */
        public Where isDateGreaterThan(final Date value) {
            if (!(col instanceof DateColumn)) {
                throw new IllegalArgumentException("Column " + col + " is not a date column!");
            }
            matcher = new DateGreaterThan((DateColumn) col, value);
            return where;
        }

        /**
         * The date must be less than the value.
         * @param value the value
         * @return the where
         */
        public Where isDateLessThan(final Date value) {
            matcher = new DateLessThan((DateColumn) col, value);
            return where;
        }

        /**
         * The year part of the date must be equal to the value.
         * @param value the value
         * @return the where
         */
        public Where isYear(final int value) {
            if (col instanceof DateColumn) {
                matcher = new YearIsMatcher((DateColumn) col, value);
            } else {
                throw new IllegalArgumentException("Column " + col + " is not dateformat!");
            }
            return where;
        }

        /**
         * The value must be among the values.
         *
         * @param values the value
         * @return the where
         */
        public Where in(final String... values) {
            matcher = new StringInMatcher((StringColumn) col, values);
            return where;
        }

        /**
         * The value must be among the values.
         * @param values the value
         * @return the where
         */
        public Where isIn(final Date... values) {
            if (col instanceof DateColumn) {
                DateColumn dc = (DateColumn) col;
                matcher = new DateIsInMatcher(dc, values);
            } else {
                throw new IllegalArgumentException("Column " + col + " is not dateformat!");
            }
            return where;
        }

    }

    /**
     * Controls the row matchers.
     *
     */
    public final class Where {

        /**
         * The list of columns.
         */
        private final List<Column> columns;
        /** The query it belongs to. */
        private final OrderBy orderBy;
        /** The query it belongs to. */
        private Query query;

        /**
         * Builds a new Where instance.
         */
        public Where() {
            columns = new ArrayList<>();
            orderBy = new OrderBy(this);
            query = null;
        }

        /**
         * Adds a new column with the columnName.
         *
         * @param columnName the column
         * @return the new instance
         */
        public Column column(final String columnName) {
            org.laukvik.csv.columns.Column col = csv.getColumn(columnName);
            Column c = new Column(col);
            c.where = this;
            columns.add(c);
            return c;
        }

        /**
         * Adds a new column.
         *
         * @param col the column
         * @return the new instance
         */
        public Column column(final org.laukvik.csv.columns.Column col) {
            Column c = new Column(col);
            c.where = this;
            columns.add(c);
            return c;
        }

        /**
         * Returns the OrderBy instance.
         *
         * @return the OrderBy
         */
        public OrderBy orderBy() {
            return orderBy;
        }

        /**
         * Returns the results.
         *
         * @return the results
         */
        public List<Row> getResultList() {
            return query.getResultList();
        }

    }

    /**
     * Controls the list of sort orders.
     *
     */
    public final class OrderBy {

        /** The Where instance it belongs to. */
        private final Where where;
        /**
         * The list of sort orders.
         */
        private final List<SortOrder> sortOrders;

        /**
         * Builds a new OrderBy control.
         *
         * @param where the Where instance.
         */
        public OrderBy(final Where where) {
            this.where = where;
            sortOrders = new ArrayList<>();
        }

        /**
         * Returns true if there are no sort orders.
         *
         * @return true if no sort orders
         */
        public boolean isEmpty() {
            return sortOrders.isEmpty();
        }

        /**
         * Sorts using ascending order.
         *
         * @param column the column to sort
         * @return OrderBy
         */
        public OrderBy asc(final org.laukvik.csv.columns.Column column) {
            sortOrders.add(new SortOrder(column, SortOrder.ASC));
            return this;
        }

        /**
         * Sorts using descending order.
         *
         * @param column the column to sort
         * @return OrderBy
         */
        public OrderBy desc(final org.laukvik.csv.columns.Column column) {
            sortOrders.add(new SortOrder(column, SortOrder.DESC));
            return this;
        }

        /**
         * Returns the results.
         *
         * @return the results
         */
        public List<Row> getResultList() {
            return where.query.getResultList();
        }

    }

    /**
     * Decides whether or not to include a column in a resultset.
     *
     */
    public final class Select {

        /** The columns. */
        private org.laukvik.csv.columns.Column[] columns;
        /**
         * The where control.
         */
        private Where where;

        /**
         * Includes only the columns in the results.
         *
         * @param columns the columns
         */
        public Select(final org.laukvik.csv.columns.Column... columns) {
            this.columns = columns;
        }

        /**
         * Returns the Where control.
         *
         * @return where
         */
        public Where where() {
            return where;
        }
    }

}
