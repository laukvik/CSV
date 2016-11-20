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

        }
        catch (Exception ex) {
        }
    }

    @Test
    public void readMetaData() {
        assertNotNull(presidency);
    }

    @Test
    public void is() {
        Query q = new Query();
        q.is(presidency,10);
        List<Row> rows = csv.getRowsByQuery(q);
        assertEquals(1, rows.size());
    }

    @Test
    public void between() {
        Query q = new Query();
        q.isBetween(presidency,10, 19);
        List<Row> rows = csv.getRowsByQuery(q);
        assertEquals(10, rows.size());
    }

    @Test
    public void isGreaterThan() {
        Query q = new Query();
        q.isGreaterThan(presidency, 40);
        List<Row> rows = csv.getRowsByQuery(q);
        assertEquals(4, rows.size());
    }

    @Test
    public void isLessThan() {
        Query q = new Query();
        q.lessThan(presidency, 41);
        List<Row> rows = csv.getRowsByQuery(q);
        assertEquals(40, rows.size());
//        List<Row> rows = csv.findByQuery().where().column(presidency).isLessThan(41).getResultList();
//        assertEquals("Should be 40", 40, rows.size());
    }

    @Test
    public void isIn() {
        Query q = new Query();
        q.isIn(presidency, 1,3,5);
        List<Row> rows = csv.getRowsByQuery(q);
        assertEquals(3, rows.size());
//        List<Row> rows = csv.findByQuery().where().column(presidency).isIn(1, 3, 5).getResultList();
//        assertEquals("Should be 3", 3, rows.size());
    }

    @Test
    public void isDate() throws ParseException {
        Date date = tookOffice.parse("20/01/2009");
        Query q = new Query();
        q.is(tookOffice, date);
        List<Row> rows = csv.getRowsByQuery(q);
        assertEquals(1, rows.size());

//        Date date = tookOffice.parse("20/01/2009");
//        List<Row> rows = csv.findByQuery().where().column(tookOffice).isDate(date).getResultList();
//        assertEquals("Should be 1", 1, rows.size());
//        fail("Lag isDate");
    }

    @Test
    public void isDateGreater() throws ParseException {
        Date date = new GregorianCalendar(2000, 1, 1).getTime();
        Query q = new Query();
        q.greaterThan(tookOffice, date);
        List<Row> rows = csv.getRowsByQuery(q);
        assertEquals(2, rows.size());
//        Date date = new GregorianCalendar(2000, 1, 1).getTime();
//        List<Row> rows = csv.findByQuery().where().column(tookOffice).isDateGreaterThan(date).getResultList();
//        assertEquals("Should be 2", 2, rows.size());
    }

    @Test
    public void isDateLess() throws ParseException {
        Date date = tookOffice.parse("1/1/1800");
        Query q = new Query();
        q.lessThan(tookOffice, date);
        List<Row> rows = csv.getRowsByQuery(q);
        assertEquals(2, rows.size());
//        List<Row> rows = csv.findByQuery().where().column(tookOffice).isDateLessThan(date).getResultList();
//        assertEquals("Should be 2", 2, rows.size());
    }

    @Test
    public void sortDesc() throws ParseException {
        Query q = new Query();
        q.descending(president);
        List<Row> rows = csv.getRowsByQuery(q);
        assertEquals("Zachary Taylor", rows.get(0).getString(president));
//        List<Row> rows = csv.findByQuery().orderBy().desc(president).getResultList();
//        assertEquals("Should be Zachary Taylor", "Zachary Taylor", rows.get(0).getString(president));
    }

    @Test
    public void sortAsc() throws ParseException, IOException {
        Query q = new Query();
        q.ascending(president);
        List<Row> rows = csv.getRowsByQuery(q);

//        csv.readFile(getResource("presidents.csv"));
//        StringColumn president = (StringColumn) csv.getColumn("President");
//        List<Row> rows = csv.findByQuery().orderBy().asc(president).getResultList();
        assertEquals("Abraham Lincoln", rows.get(0).getString(president));
    }

    @Test
    public void sortDate() throws ParseException {
        Query q = new Query();
        q.descending(tookOffice);
        List<Row> rows = csv.getRowsByQuery(q);

//        List<Row> rows = csv.findByQuery().orderBy().desc(tookOffice).getResultList();
        assertEquals("Barack Obama", rows.get(0).getString(president));
    }

    @Test
    public void stringIs() throws ParseException {
        Query q = new Query();
        q.is(homeState, "Virginia");
        List<Row> rows = csv.getRowsByQuery(q);
//        List<Row> rows = csv.findByQuery().where().column(homeState).is("Virginia").orderBy().asc(president).getResultList();
        assertEquals(5, rows.size());
    }

    @Test
    public void isEmpty() throws ParseException {
        Query q = new Query();
        q.isEmpty(leftOffice);
        List<Row> rows = csv.getRowsByQuery(q);
//        List<Row> rows = csv.findByQuery().where().column(leftOffice).isEmpty().getResultList();
        assertEquals(1, rows.size());
    }

    @Test
    public void isYear() throws ParseException {
        Query q = new Query();
        q.isYear(leftOffice, 1809);
        List<Row> rows = csv.getRowsByQuery(q);
//        List<Row> rows = csv.findByQuery().where().column(leftOffice).isYear(1809).getResultList();
        assertEquals(1, rows.size());
    }

    @Test
    public void sortAscending() throws IOException {
        Query q = new Query();
    }


    @Test
    public void isBetween() throws IOException {
        Query q = new Query();
        q.isBetween(presidency, 1, 10);
        assertEquals(10, q.getRows(csv).size());
    }

}
