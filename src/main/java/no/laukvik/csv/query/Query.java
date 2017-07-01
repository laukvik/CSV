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
package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.BigDecimalColumn;
import no.laukvik.csv.columns.BooleanColumn;
import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.DateColumn;
import no.laukvik.csv.columns.DoubleColumn;
import no.laukvik.csv.columns.FloatColumn;
import no.laukvik.csv.columns.IntegerColumn;
import no.laukvik.csv.columns.StringColumn;
import no.laukvik.csv.columns.UrlColumn;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Query is a type safe query to applying filters to the rows.
 * <h3>Using query</h3>
 * <pre>{@code
 *     Query query = new Query();
 *     query.isBetween(presidency, 1, 10);
 *     List<Row> rows = csv.findRowsByQuery( query );
 * }</pre>
 * <h3>Generic</h3>
 * <pre>{@code
 *     query.isEmpty( column );
 *     query.isNotEmpty( column );
 * }</pre>
 * <h3>String</h3>
 * <pre>{@code
 *     query.is( column, "Bob" );
 *     query.is( column, "Bob", "John" );
 *     query.isLength( column, 2 );
 *     query.isFirstletter( column, "" );
 *     query.isWordCount( column, 2 );
 * }</pre>
 * <h3>Date</h3>
 * <pre>{@code
 *     query.is( column, today );
 *     query.isBetween( column, fromDate, toDate );
 *     query.isAfter( column, today );
 *     query.isBefore( column, today );
 *     query.isYear( leftOffice, 1809 );
 *     query.isMonth( leftOffice, 1 );
 *     query.isDayOfMonth( leftOffice, 1 );
 *     query.isHour( leftOffice, 5 );
 *     query.isMinute( leftOffice, 5 );
 *     query.isSecond( leftOffice, 5 );
 *     query.isMillisecond( leftOffice, 5 );
 *     query.isWeekday( leftOffice, 5 );
 * }</pre>
 * <h3>URL</h3>
 * <pre>{@code
 *     query.is( wikipedia, new URL("http://wikipedia.org") );
 *     query.isAnchor( wikipedia, "anchor" );
 *     query.isFile( wikipedia, "test.jpg" );
 *     query.isPrefix( wikipedia, "test.jpg" );
 *     query.isPostfix( wikipedia, "test.jpg" );
 *     query.isHost( wikipedia, "test.jpg" );
 *     query.isPort( wikipedia, "test.jpg" );
 *     query.isProtocol( wikipedia, "test.jpg" );
 *     query.isQuery( wikipedia, "test.jpg" );
 * }</pre>
 * <h3>Integer</h3>
 * <pre>{@code
 *     query.is( column, 12 );
 *     query.isBetween( column, 10, 20 );
 *     query.isGreaterThan( column, 20 );
 *     query.isLessThan( column, 10 );
 * }</pre>
 * <h3>Double</h3>
 * <pre>{@code
 *     query.is( column, 12 );
 *     query.isBetween( column, 10, 20 );
 *     query.isGreaterThan( column, 20 );
 *     query.isLessThan( column, 10 );
 * }</pre>
 * <h3>Float</h3>
 * <pre>{@code
 *     query.is( column, 12 );
 *     query.isBetween( column, 10, 20 );
 *     query.isGreaterThan( column, 20 );
 *     query.isLessThan( column, 10 );
 * }</pre>
 * <h3>BigDecimal</h3>
 * <pre>{@code
 *     query.is( column, 12 );
 *     query.isBetween( column, 10, 20 );
 *     query.isGreaterThan( column, 20 );
 *     query.isLessThan( column, 10 );
 * }</pre>
 * <h3>Boolean</h3>
 * <pre>{@code
 *     query.is( column, Boolean.TRUE );
 * }</pre>
 * <h3>Sorting</h3>
 * <pre>{@code
 *     query.descending( president );
 *     query.ascending( leftOffice );
 * }</pre>
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
     * Matches empty values.
     *
     * @param column the column
     * @return the query
     */
    public Query isEmpty(final Column column) {
        addMatcher(new EmptyMatcher(column));
        return this;
    }

    /**
     * Matches non empty values.
     *
     * @param column the column
     * @return the query
     */
    public Query isNotEmpty(final Column column) {
        addMatcher(new NotEmptyMatcher(column));
        return this;
    }


    // ************* BIGDECIMAL ************************************************************************************

    /**
     * Matches values equal to.
     *
     * @param column the column
     * @param value  the value
     * @return the query
     */
    public Query is(final BigDecimalColumn column, final BigDecimal... value) {
        addMatcher(new BigDecimalMatcher(column, value));
        return this;
    }

    /**
     * Matches a value between a minimum and maximum value.
     *
     * @param column the column
     * @param min the minimum value
     * @param max the maximum value
     * @return the query
     */
    public Query isBetween(final BigDecimalColumn column, final BigDecimal min, final BigDecimal max) {
        addMatcher(new BigDecimalBetweenMatcher(column, min, max));
        return this;
    }

    /**
     * Matches values less than.
     *
     * @param column the column
     * @param value the value
     * @return the query
     */
    public Query isLessThan(final BigDecimalColumn column, final BigDecimal value) {
        addMatcher(new BigDecimalLessThanMatcher(column, value));
        return this;
    }

    /**
     * Matches values greater than.
     *
     * @param column the column
     * @param value the value
     * @return the query
     */
    public Query isGreaterThan(final BigDecimalColumn column, final BigDecimal value) {
        addMatcher(new BigDecimalGreaterThanMatcher(column, value));
        return this;
    }


    // ************* DOUBLE ************************************************************************************

    /**
     * Matches values equal to.
     *
     * @param column the column
     * @param value  the value
     * @return the query
     */
    public Query is(final DoubleColumn column, final Double... value) {
        addMatcher(new DoubleMatcher(column, value));
        return this;
    }


    /**
     * Matches a value between a minimum and maximum value.
     *
     * @param column the column
     * @param min the minimum value
     * @param max the maximum value
     * @return the query
     */
    public Query isBetween(final DoubleColumn column, final Double min, final Double max) {
        addMatcher(new DoubleBetweenMatcher(column, min, max));
        return this;
    }

    /**
     * Matches values less than.
     * @param column the column
     * @param value the value
     * @return the query
     */
    public Query isLessThan(final DoubleColumn column, final Double value) {
        addMatcher(new DoubleLessThanMatcher(column, value));
        return this;
    }

    /**
     * Matches values greater than.
     * @param column the column
     * @param value the value
     * @return the query
     */
    public Query isGreaterThan(final DoubleColumn column, final Double value) {
        addMatcher(new DoubleGreaterThanMatcher(column, value));
        return this;
    }


    // ************* STRING ************************************************************************************

    /**
     * Matches values equal to.
     *
     * @param column the column
     * @param value the value
     * @return the query
     */
    public Query is(final StringColumn column, final String... value) {
        addMatcher(new StringInMatcher(column, value));
        return this;
    }

    /**
     * Matches the first letter.
     *
     * @param column the column
     * @param value  the value
     * @return the query
     */
    public Query isFirstletter(final StringColumn column, final String... value) {
        addMatcher(new FirstLetterMatcher(column, value));
        return this;
    }

    /**
     * Matches the String length.
     *
     * @param column the column
     * @param value  the value
     * @return the query
     */
    public Query isLength(final StringColumn column, final Integer... value) {
        addMatcher(new StringLengthMatcher(column, value));
        return this;
    }

    /**
     * Matches the number of words.
     *
     * @param column the column
     * @param value  the value
     * @return the query
     */
    public Query isWordCount(final StringColumn column, final Integer... value) {
        addMatcher(new WordCountMatcher(column, value));
        return this;
    }

    /**
     * Matches using a regular expression pattern.
     *
     * @param column the column
     * @param pattern the regular expression pattern
     * @return the query
     */
    public Query isRegEx(final StringColumn column, final String pattern) {
        addMatcher(new RegExMatcher(column, Pattern.compile(pattern)));
        return this;
    }


    // ************* FLOAT ************************************************************************************

    /**
     * Matches values equal to.
     *
     * @param column the column
     * @param value the value
     * @return the query
     */
    public Query is(final FloatColumn column, final Float... value) {
        addMatcher(new FloatMatcher(column, value));
        return this;
    }

    /**
     * Matches a value between a minimum and maximum value.
     *
     * @param column the column
     * @param min the minimum value
     * @param max the maximum value
     * @return the query
     */
    public Query isBetween(final FloatColumn column, final Float min, final Float max) {
        addMatcher(new FloatBetweenMatcher(column, min, max));
        return this;
    }

    /**
     * Matches values less than.
     * @param column the column
     * @param value the value
     * @return the query
     */
    public Query isLessThan(final FloatColumn column, final Float value) {
        addMatcher(new FloatLessThanMatcher(column, value));
        return this;
    }

    /**
     * Matches values greater than.
     * @param column the column
     * @param value the value
     * @return the query
     */
    public Query isGreaterThan(final FloatColumn column, final Float value) {
        addMatcher(new FloatGreaterThanMatcher(column, value));
        return this;
    }


    // ************* INTEGER ************************************************************************************


    /**
     * Matches values equal to.
     *
     * @param column the column
     * @param value   the list of integers
     * @return the query
     */
    public Query is(final IntegerColumn column, final Integer... value) {
        addMatcher(new IntegerIsInMatcher(column, value));
        return this;
    }

    /**
     * Matches a value between a minimum and maximum value.
     *
     * @param column the column
     * @param min the minimum value
     * @param max the maximum value
     * @return the query
     */
    public Query isBetween(final IntegerColumn column, final int min, final int max) {
        addMatcher(new IntegerBetweenMatcher(column, min, max));
        return this;
    }

    /**
     * Matches values less than.
     *
     * @param column the column
     * @param value  the value
     * @return the query
     */
    public Query isLessThan(final IntegerColumn column, final int value) {
        addMatcher(new IntegerLessThanMatcher(column, value));
        return this;
    }

    /**
     * Matches values greater than.
     *
     * @param column the column
     * @param value  the value
     * @return the query
     */
    public Query isGreaterThan(final IntegerColumn column, final int value) {
        addMatcher(new IntegerGreaterThanMatcher(column, value));
        return this;
    }


    // ************* DATE ************************************************************************************

    /**
     * Matches values equal to.
     *
     * @param column the column
     * @param dates  the dates
     * @return the query
     */
    public Query is(final DateColumn column, final Date... dates) {
        addMatcher(new DateIsInMatcher(column, dates));
        return this;
    }

    /**
     * Matches a value between a minimum and maximum value.
     *
     * @param column the column
     * @param min the minimum value
     * @param max the maximum value
     * @return the query
     */
    public Query isBetween(final DateColumn column, final Date min, final Date max) {
        addMatcher(new DateBetweenMatcher(column, min, max));
        return this;
    }

    /**
     * Matches dates before.
     *
     * @param column the column
     * @param date   the date
     * @return the query
     */
    public Query isBefore(final DateColumn column, final Date date) {
        addMatcher(new DateLessThanMatcher(column, date));
        return this;
    }

    /**
     * Matches dates after.
     *
     * @param column the column
     * @param date   the date
     * @return the query
     */
    public Query isAfter(final DateColumn column, final Date date) {
        addMatcher(new DateGreaterThanMatcher(column, date));
        return this;
    }


    /**
     * Matches the year.
     *
     * @param column the column
     * @param year   the year
     * @return the query
     */
    public Query isYear(final DateColumn column, final Integer... year) {
        addMatcher(new YearMatcher(column, year));
        return this;
    }

    /**
     * Matches the month.
     *
     * @param column the column
     * @param months the month
     * @return the query
     */
    public Query isMonth(final DateColumn column, final Integer... months) {
        addMatcher(new MonthMatcher(column, months));
        return this;
    }

    /**
     * Matches the week number.
     *
     * @param column the column
     * @param weeks  the weeks
     * @return the query
     */
    public Query isWeek(final DateColumn column, final Integer... weeks) {
        addMatcher(new WeekMatcher(column, weeks));
        return this;
    }

    /**
     * Matches the day of month.
     *
     * @param column the column
     * @param days the day of month
     * @return the query
     */
    public Query isDayOfMonth(final DateColumn column, final Integer... days) {
        addMatcher(new DateOfMonthMatcher(column, days));
        return this;
    }

    /**
     * Matches the weekday.
     *
     * @param column the column
     * @param weekdays the weekdays
     * @return the query
     */
    public Query isWeekday(final DateColumn column, final Integer... weekdays) {
        addMatcher(new WeekdayMatcher(column, weekdays));
        return this;
    }

    /**
     * Matches the hour.
     *
     * @param column the column
     * @param hours the hours
     * @return the query
     */
    public Query isHour(final DateColumn column, final Integer... hours) {
        addMatcher(new HourMatcher(column, hours));
        return this;
    }

    /**
     * Matches the minute.
     *
     * @param column the column
     * @param minutes the minutes
     * @return the query
     */
    public Query isMinute(final DateColumn column, final Integer... minutes) {
        addMatcher(new MinuteMatcher(column, minutes));
        return this;
    }

    /**
     * Matches the seconds.
     *
     * @param column the column
     * @param seconds the seconds
     * @return the query
     */
    public Query isSecond(final DateColumn column, final Integer... seconds) {
        addMatcher(new SecondMatcher(column, seconds));
        return this;
    }

    /**
     * Matches the millisecond.
     *
     * @param column the column
     * @param millis the milliseconds
     * @return the query
     */
    public Query isMillisecond(final DateColumn column, final Integer... millis) {
        addMatcher(new MillisecondMatcher(column, millis));
        return this;
    }


    // ************* URL ************************************************************************************

    /**
     * Matches values equal to.
     *
     * @param column the column
     * @param urls the URLs
     * @return the query
     */
    public Query is(final UrlColumn column, final URL... urls) {
        addMatcher(new UrlMatcher(column, urls));
        return this;
    }

    /**
     * Matches the anchor of a URL.
     * @param column the column
     * @param anchors the anchors
     * @return the query
     */
    public Query isAnchor(final UrlColumn column, final String... anchors) {
        addMatcher(new UrlAnchorMatcher(column, anchors));
        return this;
    }

    /**
     * Matches the file part of a URL.
     * @param column the column
     * @param files the files
     * @return the query
     */
    public Query isFile(final UrlColumn column, final String... files) {
        addMatcher(new UrlFileMatcher(column, files));
        return this;
    }

    /**
     * Matches the prefix of a URL.
     *
     * @param column the column
     * @param prefixes the prefixes
     * @return the query
     */
    public Query isPrefix(final UrlColumn column, final String... prefixes) {
        addMatcher(new UrlFilePrefixMatcher(column, prefixes));
        return this;
    }

    /**
     * Matches the postfix of a URL.
     *
     * @param column the column
     * @param postfixes the postfixes
     * @return the query
     */
    public Query isPostfix(final UrlColumn column, final String... postfixes) {
        addMatcher(new UrlFilePrefixMatcher(column, postfixes));
        return this;
    }

    /**
     * Matches the host part of a URL.
     *
     * @param column the column
     * @param hostnames the hostnames
     * @return the query
     */
    public Query isHost(final UrlColumn column, final String... hostnames) {
        addMatcher(new UrlHostMatcher(column, hostnames));
        return this;
    }

    /**
     * Matches the port part of a URL.
     * @param column the column
     * @param port the ports
     * @return the query
     */
    public Query isPort(final UrlColumn column, final Integer... port) {
        addMatcher(new UrlPortMatcher(column, port));
        return this;
    }

    /**
     * Matches the protocol part of a URL.
     *
     * @param column the column
     * @param protocol the protocols
     * @return the query
     */
    public Query isProtocol(final UrlColumn column, final String... protocol) {
        addMatcher(new UrlProtocolMatcher(column, protocol));
        return this;
    }

    /**
     * Matches the query part of a URL.
     *
     * @param column the column
     * @param query the queries
     * @return the query
     */
    public Query isQuery(final UrlColumn column, final String... query) {
        addMatcher(new UrlQueryMatcher(column, query));
        return this;
    }

    // ************* BOOLEAN ************************************************************************************

    /**
     * Matches values equal to.
     *
     * @param column the column
     * @param booleans the booleans
     * @return the query
     */
    public Query is(final BooleanColumn column, final Boolean... booleans) {
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
    public Query ascending(final Column column) {
        addSort(new SortOrder(column, SortDirection.ASC));
        return this;
    }

    /**
     * Sorts the rows in descending order for the column.
     *
     * @param column the column
     * @return the same query instance
     */
    public Query descending(final Column column) {
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
            if (matcher.matches(row.getObject(c))) {
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
