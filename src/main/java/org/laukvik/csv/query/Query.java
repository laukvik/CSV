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
import org.laukvik.csv.columns.Column;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * The query contains the criteria to be used when filtering the data set.
 */
public final class Query {

    /**
     * Contains all matchers.
     */
    private final List<ValueMatcher> matchers;
    /**
     * Contains all sort orders.
     */
    private final List<SortOrder> sorters;

    /**
     * Builds a new empty query.
     */
    public Query() {
        matchers = new ArrayList<>();
        sorters = new ArrayList<>();
    }

    /**
     * The value in the column must be after the date.
     *
     * @param column the column
     * @param date   the date
     * @return the same query instance
     */
    public Query after(final org.laukvik.csv.columns.DateColumn column, final Date date) {
        addRowMatcher(new DateGreaterThanMatcher(column, date));
        return this;
    }

    /**
     * The value in the column must match either of the dates.
     *
     * @param column the column
     * @param dates  the dates
     * @return the same query instance
     */
    public Query contains(final org.laukvik.csv.columns.DateColumn column, final Date... dates) {
        addRowMatcher(new DateIsInMatcher(column, dates));
        return this;
    }

    /**
     * The value in the column must be the same as the date.
     *
     * @param column the column
     * @param date   the date
     * @return the same query instance
     */
    public Query is(final org.laukvik.csv.columns.DateColumn column, final Date date) {
        addRowMatcher(new DateIsMatcher(column, date));
        return this;
    }

    /**
     * The value in the column must be the same as the value.
     *
     * @param column the column
     * @param value  the value
     * @return the same query instance
     */
    public Query is(final org.laukvik.csv.columns.StringColumn column, final String value) {
        addRowMatcher(new StringInMatcher(column, value));
        return this;
    }

    /**
     * The value in the column must be in the list.
     *
     * @param column the column
     * @param list   the list of values
     * @return the same query instance
     */
    public Query in(final org.laukvik.csv.columns.StringColumn column, final String... list) {
        addRowMatcher(new StringInMatcher(column, list));
        return this;
    }

    /**
     * The value in the column must be lessThan the date.
     *
     * @param column the column
     * @param date   the date
     * @return the same query instance
     */
    public Query lessThan(final org.laukvik.csv.columns.DateColumn column, final Date date) {
        addRowMatcher(new DateLessThanMatcher(column, date));
        return this;
    }

    /**
     * The value in the column must be empty.
     *
     * @param column the column
     * @return the same query instance
     */
    public Query isEmpty(final org.laukvik.csv.columns.Column column) {
        addRowMatcher(new EmptyMatcher(column));
        return this;
    }

    /**
     * The value in the column must be same or more than minimum and less than or same as maximum.
     *
     * @param column the column
     * @param min    the minimum value
     * @param max    the maximum value
     * @return the same query instance
     */
    public Query isBetween(final org.laukvik.csv.columns.IntegerColumn column, final int min, final int max) {
        addRowMatcher(new IntegerBetweenMatcher(column, min, max));
        return this;
    }

    /**
     * The value in the column must be greater than the value.
     *
     * @param column the column
     * @param value  the value
     * @return the same query instance
     */
    public Query isGreaterThan(final org.laukvik.csv.columns.IntegerColumn column, final int value) {
        addRowMatcher(new IntegerGreaterThanMatcher(column, value));
        return this;
    }

    /**
     * The value in the column must be in the list.
     *
     * @param column the column
     * @param list   the list of integers
     * @return the same query instance
     */
    public Query isIn(final org.laukvik.csv.columns.IntegerColumn column, final Integer... list) {
        addRowMatcher(new IntegerIsInMatcher(column, list));
        return this;
    }

    /**
     * The value in the column must be same as the value.
     *
     * @param column the column
     * @param value  the value
     * @return the same query instance
     */
    public Query is(final org.laukvik.csv.columns.IntegerColumn column, final Integer value) {
        addRowMatcher(new IntegerIsMatcher(column, value));
        return this;
    }

    /**
     * The value in the column must be less thn the value.
     *
     * @param column the column
     * @param value  the value
     * @return the same query instance
     */
    public Query lessThan(final org.laukvik.csv.columns.IntegerColumn column, final int value) {
        addRowMatcher(new IntegerLessThanMatcher(column, value));
        return this;
    }

    /**
     * The value in the column must be in the list.
     *
     * @param column the column
     * @param list   the list of Float
     * @return the same query instance
     */
    public Query in(final org.laukvik.csv.columns.Column column, final Float... list) {
        addRowMatcher(new IsInMatcher<Float>(column, list));
        return this;
    }

    /**
     * The value in the column must be non null.
     *
     * @param column the column
     * @return the same query instance
     */
    public Query notEmpty(final org.laukvik.csv.columns.Column column) {
        addRowMatcher(new NotEmptyMatcher(column));
        return this;
    }

    /**
     * The year in the column must be the same as year.
     *
     * @param column the column
     * @param year   the year
     * @return the same query instance
     */
    public Query isYear(final org.laukvik.csv.columns.DateColumn column, final Integer year) {
        addRowMatcher(new YearMatcher(column, year));
        return this;
    }

    /**
     * Adds the matcher.
     *
     * @param matcher the matcher
     */
    public void addRowMatcher(final ValueMatcher matcher) {
        matchers.add(matcher);
    }

    /**
     * Remoes the matcher.
     *
     * @param matcher the matcher
     */
    public void removeRowMatcher(final ValueMatcher matcher) {
        matchers.remove(matcher);
    }

    /**
     * Sorts the rows in ascending order for the column.
     *
     * @param column the column
     * @return the same query instance
     */
    public Query ascending(final org.laukvik.csv.columns.Column column) {
        addSort(new SortOrder(column, SortDirection.ASC));
        return this;
    }

    /**
     * Sorts the rows in descending order for the column.
     *
     * @param column the column
     * @return the same query instance
     */
    public Query descending(final org.laukvik.csv.columns.Column column) {
        addSort(new SortOrder(column, SortDirection.DESC));
        return this;
    }

    /**
     * Adds the sortOrder.
     *
     * @param sortOrder the column to sort
     */
    public void addSort(final SortOrder sortOrder) {
        sorters.add(sortOrder);
    }

    /**
     * Removes the sortOrder.
     *
     * @param sortOrder the sortOrder to add
     */
    public void removeSort(final SortOrder sortOrder) {
        sorters.remove(sortOrder);
    }

    /**
     * Returns all matchers.
     *
     * @return the matchers
     */
    public List<ValueMatcher> getMatchers() {
        return matchers;
    }

    /**
     * Returns all sort orders.
     *
     * @return the sort orders
     */
    public List<SortOrder> getSorters() {
        return sorters;
    }

    /**
     * Returns true if the row matchesRow the query.
     *
     * @param row the row
     * @return true when matchesRow
     */
    public boolean matches(final Row row) {
        int matchCount = 0;

        for (ValueMatcher matcher : matchers) {
            Column c = matcher.getColumn();
            if (matcher.matches(row.get(c))){
                matchCount++;
            }
//            if (matcher.matches(row)) {
//                matchCount++;
//            }
        }
        return matchCount == matchers.size();
    }

    /**
     * Returns the matching rows.
     *
     * @param csv the csv
     * @return the rows
     */
    public List<Row> getRows(final CSV csv) {
        List<Row> filteredRows = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < csv.getRowCount(); rowIndex++) {
            Row r = csv.getRow(rowIndex);
            if (matches(r)) {
                filteredRows.add(r);
            }
        }
        if (!sorters.isEmpty()) {
            Collections.sort(filteredRows, new RowSorter(sorters));
        }
        return filteredRows;
    }

}
