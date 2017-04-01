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

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The query contains the criteria to be used when filtering the data set.
 * <p>
 * Column
 * DONE - is empty
 * DONE - not Empty
 * <p>
 * String
 * DONE - firstletter
 * DONE - is in
 * DONE - length
 * DONE - word count
 * <p>
 * Boolean
 * DONE - is/in
 * <p>
 * Integer
 * DONE - is
 * DONE - between
 * DONE - greater than
 * DONE - less than
 * <p>
 * BigDecimal
 * DONE - is
 * TODO - between
 * TODO - greater than
 * TODO - less than
 * <p>
 * Float
 * DONE - in
 * TODO - between
 * TODO - greater than
 * TODO - less than
 * <p>
 * Double
 * DONE - is
 * TODO - between
 * TODO - greater than
 * TODO - less than
 * <p>
 * Date
 * TODO - date between
 * DONE - greater than (isAfter)
 * DONE - date is (contains)
 * DONE - date less (isBefore)
 * DONE - date of month
 * DONE - hour
 * DONE - millisecond
 * DONE - minute
 * DONE - month
 * DONE - second
 * DONE - weekday
 * DONE - week isWeek
 * DONE - year (isYear)
 * <p>
 * URL
 * DONE - is
 * DONE - anchor
 * DONE - file
 * DONE - file postfix
 * DONE - file prefix
 * DONE - host
 * DONE - port
 * DONE - protocol
 * DONE - query
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


    // ************* COLUMN ************************************************************************************

    /**
     * The value in the column must be empty.
     *
     * @param column the column
     * @return the same query instance
     */
    public Query isEmpty(final org.laukvik.csv.columns.Column column) {
        addMatcher(new EmptyMatcher(column));
        return this;
    }

    /**
     * The value in the column must be non null.
     *
     * @param column the column
     * @return the same query instance
     */
    public Query notEmpty(final org.laukvik.csv.columns.Column column) {
        addMatcher(new NotEmptyMatcher(column));
        return this;
    }


    // ************* BIGDECIMAL ************************************************************************************

    public Query is(final org.laukvik.csv.columns.BigDecimalColumn column, final BigDecimal... list) {
        addMatcher(new BigDecimalMatcher(column, list));
        return this;
    }

    public Query isBetween(final org.laukvik.csv.columns.BigDecimalColumn column, final BigDecimal min, final BigDecimal max) {
        addMatcher(new BigDecimalBetweenMatcher(column, min, max));
        return this;
    }

    public Query isLessThan(final org.laukvik.csv.columns.BigDecimalColumn column, final BigDecimal value) {
        addMatcher(new BigDecimalLessThanMatcher(column, value));
        return this;
    }

    public Query isGreaterThan(final org.laukvik.csv.columns.BigDecimalColumn column, final BigDecimal value) {
        addMatcher(new BigDecimalGreaterThanMatcher(column, value));
        return this;
    }


    // ************* DOUBLE ************************************************************************************

    public Query is(final org.laukvik.csv.columns.DoubleColumn column, final Double... list) {
        addMatcher(new DoubleMatcher(column, list));
        return this;
    }


    public Query isBetween(final org.laukvik.csv.columns.DoubleColumn column, final Double min, final Double max) {
        addMatcher(new DoubleBetweenMatcher(column, min, max));
        return this;
    }

    public Query isLessThan(final org.laukvik.csv.columns.DoubleColumn column, final Double value) {
        addMatcher(new DoubleLessThanMatcher(column, value));
        return this;
    }

    public Query isGreaterThan(final org.laukvik.csv.columns.DoubleColumn column, final Double value) {
        addMatcher(new DoubleGreaterThanMatcher(column, value));
        return this;
    }


    // ************* STRING ************************************************************************************

    /**
     * The value in the column must be in the list.
     *
     * @param column the column
     * @param list   the list of values
     * @return the same query instance
     */
    public Query is(final org.laukvik.csv.columns.StringColumn column, final String... list) {
        addMatcher(new StringInMatcher(column, list));
        return this;
    }

    public Query isFirstletter(final org.laukvik.csv.columns.StringColumn column, final String... list) {
        addMatcher(new FirstLetterMatcher(column, list));
        return this;
    }

    public Query isLength(final org.laukvik.csv.columns.StringColumn column, final Integer... list) {
        addMatcher(new StringLengthMatcher(column, list));
        return this;
    }

    public Query isWordCount(final org.laukvik.csv.columns.StringColumn column, final Integer... list) {
        addMatcher(new WordCountMatcher(column, list));
        return this;
    }

    public Query isRegEx(final org.laukvik.csv.columns.StringColumn column, final String pattern) {
        addMatcher(new RegExMatcher(column, Pattern.compile(pattern)));
        return this;
    }


    // ************* FLOAT ************************************************************************************

    /**
     * The value in the column must be in the list.
     *
     * @param column the column
     * @param floats the list of Float
     * @return the same query instance
     */
    public Query is(final org.laukvik.csv.columns.FloatColumn column, final Float... floats) {
        addMatcher(new FloatMatcher(column, floats));
        return this;
    }

    public Query isBetween(final org.laukvik.csv.columns.FloatColumn column, final Float min, final Float max) {
        addMatcher(new FloatBetweenMatcher(column, min, max));
        return this;
    }

    public Query isLessThan(final org.laukvik.csv.columns.FloatColumn column, final Float value) {
        addMatcher(new FloatLessThanMatcher(column, value));
        return this;
    }

    public Query isGreaterThan(final org.laukvik.csv.columns.FloatColumn column, final Float value) {
        addMatcher(new FloatGreaterThanMatcher(column, value));
        return this;
    }


    // ************* INTEGER ************************************************************************************


    /**
     * The value in the column must be in the list.
     *
     * @param column the column
     * @param list   the list of integers
     * @return the same query instance
     */
    public Query is(final org.laukvik.csv.columns.IntegerColumn column, final Integer... list) {
        addMatcher(new IntegerIsInMatcher(column, list));
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
        addMatcher(new IntegerBetweenMatcher(column, min, max));
        return this;
    }

    /**
     * The value in the column must be less thn the value.
     *
     * @param column the column
     * @param value  the value
     * @return the same query instance
     */
    public Query isLessThan(final org.laukvik.csv.columns.IntegerColumn column, final int value) {
        addMatcher(new IntegerLessThanMatcher(column, value));
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
        addMatcher(new IntegerGreaterThanMatcher(column, value));
        return this;
    }


    // ************* DATE ************************************************************************************

    /**
     * The value in the column must match either of the dates.
     *
     * @param column the column
     * @param dates  the dates
     * @return the same query instance
     */
    public Query is(final org.laukvik.csv.columns.DateColumn column, final Date... dates) {
        addMatcher(new DateIsInMatcher(column, dates));
        return this;
    }

    public Query isBetween(final org.laukvik.csv.columns.DateColumn column, final Date min, final Date max) {
        addMatcher(new DateBetweenMatcher(column, min, max));
        return this;
    }

    /**
     * The value in the column must be isBefore the date.
     *
     * @param column the column
     * @param date   the date
     * @return the same query instance
     */
    public Query isBefore(final org.laukvik.csv.columns.DateColumn column, final Date date) {
        addMatcher(new DateLessThanMatcher(column, date));
        return this;
    }

    /**
     * The value in the column must be isAfter the date.
     *
     * @param column the column
     * @param date   the date
     * @return the same query instance
     */
    public Query isAfter(final org.laukvik.csv.columns.DateColumn column, final Date date) {
        addMatcher(new DateGreaterThanMatcher(column, date));
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
        addMatcher(new YearMatcher(column, year));
        return this;
    }

    public Query isMonth(final org.laukvik.csv.columns.DateColumn column, final Integer... months) {
        addMatcher(new MonthMatcher(column, months));
        return this;
    }

    /**
     * One of the weeks must match the value.
     *
     * @param column the column
     * @param weeks  the weeks
     * @return the same query instance
     */
    public Query isWeek(final org.laukvik.csv.columns.DateColumn column, final Integer... weeks) {
        addMatcher(new WeekMatcher(column, weeks));
        return this;
    }

    public Query isDayOfMonth(final org.laukvik.csv.columns.DateColumn column, final Integer... days) {
        addMatcher(new DateOfMonthMatcher(column, days));
        return this;
    }

    public Query isWeekday(final org.laukvik.csv.columns.DateColumn column, final Integer... weekdays) {
        addMatcher(new WeekdayMatcher(column, weekdays));
        return this;
    }

    public Query isHour(final org.laukvik.csv.columns.DateColumn column, final Integer... hours) {
        addMatcher(new HourMatcher(column, hours));
        return this;
    }

    public Query isMinute(final org.laukvik.csv.columns.DateColumn column, final Integer... minutes) {
        addMatcher(new MinuteMatcher(column, minutes));
        return this;
    }

    public Query isSecond(final org.laukvik.csv.columns.DateColumn column, final Integer... seconds) {
        addMatcher(new SecondMatcher(column, seconds));
        return this;
    }

    public Query isMillisecond(final org.laukvik.csv.columns.DateColumn column, final Integer... millis) {
        addMatcher(new MillisecondMatcher(column, millis));
        return this;
    }


    // ************* URL ************************************************************************************

    public Query is(final org.laukvik.csv.columns.UrlColumn column, final URL... urls) {
        addMatcher(new UrlMatcher(column, urls));
        return this;
    }

    public Query isAnchor(final org.laukvik.csv.columns.UrlColumn column, final String... anchors) {
        addMatcher(new UrlAnchorMatcher(column, anchors));
        return this;
    }

    public Query isFile(final org.laukvik.csv.columns.UrlColumn column, final String... files) {
        addMatcher(new UrlFileMatcher(column, files));
        return this;
    }

    public Query isPrefix(final org.laukvik.csv.columns.UrlColumn column, final String... prefixes) {
        addMatcher(new UrlFilePrefixMatcher(column, prefixes));
        return this;
    }

    public Query isPostfix(final org.laukvik.csv.columns.UrlColumn column, final String... postfixes) {
        addMatcher(new UrlFilePrefixMatcher(column, postfixes));
        return this;
    }

    public Query isHost(final org.laukvik.csv.columns.UrlColumn column, final String... hostnames) {
        addMatcher(new UrlHostMatcher(column, hostnames));
        return this;
    }

    public Query isPort(final org.laukvik.csv.columns.UrlColumn column, final Integer... port) {
        addMatcher(new UrlPortMatcher(column, port));
        return this;
    }

    public Query isProtocol(final org.laukvik.csv.columns.UrlColumn column, final String... protocol) {
        addMatcher(new UrlProtocolMatcher(column, protocol));
        return this;
    }


    // ************* BOOLEAN ************************************************************************************

    public Query is(final org.laukvik.csv.columns.BooleanColumn column, final Boolean... booleans) {
        addMatcher(new BooleanMatcher(column, booleans));
        return this;
    }


    /**
     * Adds the matcher.
     *
     * @param matcher the matcher
     */
    public void addMatcher(final ValueMatcher matcher) {
        matchers.add(matcher);
    }

    /**
     * Remoes the matcher.
     *
     * @param matcher the matcher
     */
    public void removeMatcher(final ValueMatcher matcher) {
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
            if (matcher.matches(row.get(c))) {
                matchCount++;
            }
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
            filteredRows.sort(new RowSorter(sorters));
        }
        return filteredRows;
    }

}
