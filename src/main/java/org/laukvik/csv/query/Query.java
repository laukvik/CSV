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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.laukvik.csv.CSV;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;

/**
 *
 * <code>
 * .select()
 * .where()
 * .column("BookID").is(5);
 * </code>
 *
 * <code>
 * .select()
 * .where();
 * </code>
 *
 * <code>
 * .where
 * .column("BookID").is(5)
 * .column("Chapter").isGreaterThan(3)
 * .column("Verse").isBetween(10,20)
 *
 * .orderBy
 * .asc("BookID").desc("");
 *
 * .findResults(1,10);
 *
 * </code>
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class Query {

    public class Column {

        Where where;
        String name;
        RowMatcher matcher;

        public Column(String name) {
            this.name = name;
        }

        private int getColumnIndex() {
            return where.query.metaData.getColumnIndex(name);
        }

        /* Common */
        public Where isNotEmpty() {
            matcher = new NotEmptyMatcher(getColumnIndex());
            return where;
        }

        public Where isEmpty() {
            matcher = new EmptyMatcher(getColumnIndex());
            return where;
        }

        /* String */
        public Where is(String value) {
            matcher = new StringIsMatcher(getColumnIndex(), value);
            return where;
        }

        public Where isIn(String[] value) {
            matcher = new StringIsMatcher(getColumnIndex(), value);
            return where;
        }

        /* int */
        public Where is(int value) {
            matcher = new IntIsMatcher(getColumnIndex(), value);
            return where;
        }

        public Where isBetween(int min, int max) {
            matcher = new IntBetween(getColumnIndex(), min, max);
            return where;
        }

        public Where isIn(Integer... values) {
            matcher = new IntIsInMatcher(getColumnIndex(), values);
            return where;
        }

        public Where isIn(Float[] values) {
            matcher = new IsInMatcher<Float>(getColumnIndex(), values);
            return where;
        }

        public Where isIn(Double[] values) {
            matcher = new IsInMatcher<Double>(getColumnIndex(), values);
            return where;
        }

        public Where isGreaterThan(int value) {
            matcher = new IntGreaterThanMatcher(getColumnIndex(), value);
            return where;
        }

        public Where isLessThan(int value) {
            matcher = new IntLessThan(getColumnIndex(), value);
            return where;
        }

        public Where isDate(Date value, SimpleDateFormat format) {
            matcher = new DateIsMatcher(getColumnIndex(), value, format);
            return where;
        }

        public Where isDateGreaterThan(Date value, SimpleDateFormat format) {
            matcher = new DateGreaterThan(getColumnIndex(), value, format);
            return where;
        }

        public Where isDateLessThan(Date value, SimpleDateFormat format) {
            matcher = new DateLessThan(getColumnIndex(), value, format);
            return where;
        }

        public Where isYear(int value) {
            org.laukvik.csv.columns.Column c = where.query.metaData.getColumn(name);
            if (c instanceof DateColumn) {
                DateColumn dc = (DateColumn) c;
                matcher = new YearIs(getColumnIndex(), value, dc.getFormat());
            } else {
                throw new IllegalArgumentException("Column " + name + " is not dateformat!");
            }
            return where;
        }

        public Where in(String... selection) {
            matcher = new StringIsMatcher(getColumnIndex(), selection);
            return where;
        }

        public Where isIn(Date[] arr) {
            org.laukvik.csv.columns.Column c = where.query.metaData.getColumn(name);
            if (c instanceof DateColumn) {
                DateColumn dc = (DateColumn) c;
                matcher = new DateIsInMatcher(getColumnIndex(), arr, dc.getFormat());
            } else {
                throw new IllegalArgumentException("Column " + name + " is not dateformat!");
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

        public Column column(String name) {
            Column c = new Column(name);
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

        private int getColumnIndex(String name) {
            return where.query.metaData.getColumnIndex(name);
        }

        public OrderBy asc(String column) {
            sortOrders.add(new SortOrder(getColumnIndex(column), SortOrder.ASC));
            return this;
        }

        public OrderBy desc(String column) {
            sortOrders.add(new SortOrder(getColumnIndex(column), SortOrder.DESC));
            return this;
        }

        public List<Row> getResultList() {
            return where.query.getResultList();
        }

    }

    public class Select {

        private Where where;
        private final String[] columns;

        public Select(String... columns) {
            this.columns = columns;
        }

        public Where where() {
            return where;
        }
    }

    private Where where;
    private Select select;
    private final MetaData metaData;
    private final CSV csv;

    public Query(MetaData metaData, CSV csv) {
        this.metaData = metaData;
        this.csv = csv;
    }

    public OrderBy orderBy() {
        return this.where().orderBy();
    }

    public Where where() {
        where = new Where();
        where.query = this;
        return where;
    }

    public Select select() {
        select = new Select();
        select.where = where;
        return select;
    }

    public Select select(String... columns) {
        select = new Select(columns);
        select.where = where;
        return select;
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
                    if (c.matcher.mathes(r)) {
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

}
