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

import org.junit.Before;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.CSVTest;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.columns.UrlColumn;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class QueryTest {

    CSV csv;
    SimpleDateFormat format;
    IntegerColumn presidency;
    StringColumn president;
    DateColumn tookOffice;
    DateColumn leftOffice;
    UrlColumn wikipedia;
    StringColumn homeState;
    StringColumn party;
    StringColumn portrait;
    StringColumn thumbnail;

    public static File getResource(String filename) {
        ClassLoader classLoader = CSVTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Before
    public void setup() {
        try {
            format = new SimpleDateFormat("dd/MM/yyyy");
            csv = new CSV();
            csv.readFile(getResource("metadata.csv"));
            presidency = (IntegerColumn) csv.getColumn("presidency");
            president = (StringColumn) csv.getColumn("president");
            tookOffice = (DateColumn) csv.getColumn("Took office");
            leftOffice = (DateColumn) csv.getColumn("Left office");
            wikipedia = (UrlColumn) csv.getColumn("Wikipedia Entry");
            homeState = (StringColumn) csv.getColumn("Home State");
            party = (StringColumn) csv.getColumn("Party");
            portrait = (StringColumn) csv.getColumn("portrait");
            thumbnail = (StringColumn) csv.getColumn("thumbnail");
        } catch (Exception ex) {
        }
    }

    @Test
    public void readMetaData() {
        assertNotNull(presidency);
    }

    @Test
    public void isInt() {
        Query q = new Query();
        q.is(presidency,10);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(1, rows.size());
    }

    @Test
    public void isIntBetween() {
        Query q = new Query();
        q.isBetween(presidency,10, 19);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(10, rows.size());
    }

    @Test
    public void isGreaterThan() {
        Query q = new Query();
        q.isGreaterThan(presidency, 40);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(4, rows.size());
    }

    @Test
    public void lessThan() {
        Query q = new Query();
        q.lessThan(presidency, 41);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(40, rows.size());
    }

    @Test
    public void isIn() {
        Query q = new Query();
        q.isIn(presidency, 1,3,5);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(3, rows.size());
    }

    @Test
    public void isDate() throws ParseException {
        Date date = tookOffice.parse("20/01/2009");
        Query q = new Query();
        q.is(tookOffice, date);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(1, rows.size());
    }

    @Test
    public void greaterThan() throws ParseException {
        Date date = new GregorianCalendar(2000, 1, 1).getTime();
        Query q = new Query();
        q.after(tookOffice, date);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(2, rows.size());
    }

    @Test
    public void isDateLess() throws ParseException {
        Date date = tookOffice.parse("1/1/1800");
        Query q = new Query();
        q.lessThan(tookOffice, date);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(2, rows.size());
    }




    @Test
    public void sortDate() throws ParseException {
        Query q = new Query();
        q.descending(tookOffice);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals("Barack Obama", rows.get(0).getString(president));
    }

    @Test
    public void stringIs() throws ParseException {
        Query q = new Query();
        q.is(homeState, "Virginia");
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(5, rows.size());
    }

    @Test
    public void isEmpty() throws ParseException {
        Query q = new Query();
        q.isEmpty(leftOffice);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(1, rows.size());
    }

    @Test
    public void isYear() throws ParseException {
        Query q = new Query();
        q.isYear(leftOffice, 1809);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(1, rows.size());
    }

    @Test
    public void ascending() throws IOException {
        Query q = new Query();
        q.ascending(leftOffice);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals( (Integer)44, rows.get(0).getInteger(presidency));
    }

    @Test
    public void descending() throws ParseException {
        Query q = new Query();
        q.descending(president);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals("Zachary Taylor", rows.get(0).getString(president));
    }


    @Test
    public void isBetween() throws IOException {
        Query q = new Query();
        q.isBetween(presidency, 1, 10);
        assertEquals(10, q.getRows(csv).size());
    }

    @Test
    public void greaterThan_date() throws IOException {
        Query q = new Query();
        q.after(leftOffice, leftOffice.parse("01/01/2005")); // dd/MM/yyyy
        assertEquals(1, q.getRows(csv).size());
    }

    @Test
    public void getMatchers() throws IOException {
        Query q = new Query();
        IntegerLessThanMatcher m = new IntegerLessThanMatcher(presidency, 5);
        q.addMatcher(m);
        assertEquals(1, q.getMatchers().size());
        q.removeMatcher(m);
        assertEquals(0, q.getMatchers().size());
    }

    @Test
    public void getSorters() throws IOException {
        Query q = new Query();
        SortOrder so = new SortOrder(presidency,SortDirection.ASC);
        q.addSort(so);
        assertEquals( 1, q.getSorters().size());
        q.removeSort(so);
        assertEquals( 0, q.getSorters().size());
    }

}
