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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * The query contains the criteria to be used when filtering the data set.
 * <p>
 * csv.addFilter
 * <p>
 * <p>
 * <pre>
 * query.count().where().column("BookID").is(5);
 * </pre>
 * <p>
 * <pre>
 * query.select().where().column("BookID").is(5);
 * </pre>
 * <p>
 * <pre>
 * query.select().orderBy().asc("BookID");
 * </pre>
 * <p>
 * csv.getRows(query);
 */
public final class Query {

    /**
     * Contains all matchers.
     */
    private final List<RowMatcher> matchers;
    /**
     * Contains all sort orders.
     */
    private final List<SortOrder> sorters;

    /**
     * Builds a new empty query.
     *
     */
    public Query() {
        matchers = new ArrayList<>();
        sorters = new ArrayList<>();
    }

    public Query greaterThan(final org.laukvik.csv.columns.DateColumn column, final Date date) {
        addRowMatcher(new DateGreaterThanMatcher(column, date));
        return this;
    }

    public Query contains(final org.laukvik.csv.columns.DateColumn column, final Date... dates) {
        addRowMatcher(new DateIsInMatcher(column, dates));
        return this;
    }

    public Query is(final org.laukvik.csv.columns.DateColumn column, final Date date) {
        addRowMatcher(new DateIsMatcher(column, date));
        return this;
    }

    public Query is(final org.laukvik.csv.columns.StringColumn column, final String value) {
        addRowMatcher(new StringIsMatcher(column, value));
        return this;
    }

    public Query in(final org.laukvik.csv.columns.StringColumn column, final String... values) {
        addRowMatcher(new StringInMatcher(column, values));
        return this;
    }

    public Query lessThan(final org.laukvik.csv.columns.DateColumn column, final Date date) {
        addRowMatcher(new DateLessThanMatcher(column, date));
        return this;
    }

    public Query isEmpty(final org.laukvik.csv.columns.Column column) {
        addRowMatcher(new EmptyMatcher(column));
        return this;
    }

    public Query isBetween(final org.laukvik.csv.columns.IntegerColumn column, final int min, final int max) {
        addRowMatcher(new IntBetweenMatcher(column, min, max));
        return this;
    }

    public Query isGreaterThan(final org.laukvik.csv.columns.IntegerColumn column, final int value) {
        addRowMatcher(new IntGreaterThanMatcher(column, value));
        return this;
    }

    public Query isIn(final org.laukvik.csv.columns.IntegerColumn column, final Integer... values) {
        addRowMatcher(new IntIsInMatcher(column, values));
        return this;
    }

    public Query is(final org.laukvik.csv.columns.IntegerColumn column, final int value) {
        addRowMatcher(new IntIsMatcher(column, value));
        return this;
    }

    public Query lessThan(final org.laukvik.csv.columns.IntegerColumn column, final int value) {
        addRowMatcher(new IntLessThanMatcher(column, value));
        return this;
    }

    public Query in(final org.laukvik.csv.columns.Column column, final Float... values) {
        addRowMatcher(new IsInMatcher<Float>(column, values));
        return this;
    }

    public Query notEmpty(final org.laukvik.csv.columns.Column column) {
        addRowMatcher(new NotEmptyMatcher(column));
        return this;
    }

    public Query isYear(final org.laukvik.csv.columns.DateColumn column, final int value) {
        addRowMatcher(new YearIsMatcher(column, value));
        return this;
    }

    /**
     * Adds the matcher.
     *
     * @param matcher the matcher
     */
    public void addRowMatcher(final RowMatcher matcher) {
        matchers.add(matcher);
    }

    /**
     * Remoes the matcher.
     *
     * @param matcher the matcher
     */
    public void removeRowMatcher(final RowMatcher matcher) {
        matchers.remove(matcher);
    }

    public Query ascending(final org.laukvik.csv.columns.Column column){
        addSort(column, SortDirection.ASC);
        return this;
    }

    public Query descending(final org.laukvik.csv.columns.Column column){
        addSort(column, SortDirection.DESC);
        return this;
    }

    /**
     * Adds the sortOrder.
     *
     * @param column the column to sort
     * @param direction the direction
     */
    public void addSort(final org.laukvik.csv.columns.Column column, final SortDirection direction) {
        sorters.add(new SortOrder(column, direction));
    }

    /**
     * Removes the sortOrder.
     *
     * @param sortOrder the sortOrder to add
     */
    public void removeSort(final SortOrder sortOrder) {
        sorters.remove(sortOrder);
    }

    public boolean matches(final Row row){
        int matchCount = 0;
        for (RowMatcher matcher : matchers){
            if (matcher.matches(row)){
                matchCount++;
            }
        }
        return matchCount == matchers.size();
    }

    public List<Row> getRows(final CSV csv) {
        List<Row> filteredRows = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < csv.getRowCount(); rowIndex++) {
            Row r = csv.getRow(rowIndex);
            if (matches(r)){
                filteredRows.add(r);
            }
        }
        if (!sorters.isEmpty()) {
            Collections.sort(filteredRows, new RowSorter(sorters));
        }
        return filteredRows;
    }

}
