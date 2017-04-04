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
import no.laukvik.csv.CSVTest;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.DateColumn;
import no.laukvik.csv.columns.IntegerColumn;
import no.laukvik.csv.columns.StringColumn;
import no.laukvik.csv.columns.UrlColumn;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;


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

    // --- Common --------------------------------------------------------------
    @Test
    public void isEmpty() throws ParseException {
        Query q = new Query();
        q.isEmpty(leftOffice);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(1, rows.size());
    }

    @Test
    public void isNotEmpty() throws ParseException {
        Query q = new Query();
        q.isNotEmpty(leftOffice);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(43, rows.size());
    }

    // --- Integer --------------------------------------------------------------


    @Test
    public void intIs() {
        Query q = new Query();
        q.is(presidency, 10);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(1, rows.size());
    }

    @Test
    public void intBetween() {
        Query q = new Query();
        q.isBetween(presidency, 10, 19);
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
        q.isLessThan(presidency, 41);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(40, rows.size());
    }


    // --- Float --------------------------------------------------------------

    // --- Double --------------------------------------------------------------

    // --- BigDecimal -----------------------------------------------------------


    // --- String --------------------------------------------------------------
    @Test
    public void stringIs() throws ParseException {
        Query q = new Query();
        q.is(homeState, "Virginia");
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(5, rows.size());
    }

    @Test
    public void stringFirstletter() throws ParseException {
        Query q = new Query();
        q.isFirstletter(homeState, "V");
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(5, rows.size());
    }

    @Test
    public void stringWordCount() throws ParseException {
        Query q = new Query();
        q.isWordCount(homeState, 2);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(9, rows.size());
    }

    @Test
    public void stringLength() throws ParseException {
        Query q = new Query();
        q.isLength(homeState, 10);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(3, rows.size());
    }

    // --- URL --------------------------------------------------------------

    @Test
    public void isURL() throws ParseException, MalformedURLException {
        Query q = new Query();
        q.is(wikipedia, new URL("http://nothing"));
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(0, rows.size());
    }

    @Test
    public void isAnchor() throws ParseException, MalformedURLException {
        Query q = new Query();
        q.isAnchor(wikipedia, "test");
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(0, rows.size());
    }

    @Test
    public void isFile() throws ParseException, MalformedURLException {
        Query q = new Query();
        q.isFile(wikipedia, "George_Washington");
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(1, rows.size());
    }

    @Test
    public void isUrlPrefix() throws ParseException, MalformedURLException {
        Query q = new Query();
        q.isPrefix(wikipedia, "George_Washington");
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(1, rows.size());
    }

    @Test
    public void isUrlPostfix() throws ParseException, MalformedURLException {
        Query q = new Query();
        q.isPostfix(wikipedia, "jpg");
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(0, rows.size());
    }

    @Test
    public void isHost() throws ParseException, MalformedURLException {
        Query q = new Query();
        q.isHost(wikipedia, "en.wikipedia.org");
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(42, rows.size());
    }

    @Test
    public void isPort() throws ParseException, MalformedURLException {
        Query q = new Query();
        q.isPort(wikipedia, 80);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(0, rows.size());
    }

    @Test
    public void isProtocol() throws ParseException, MalformedURLException {
        Query q = new Query();
        q.isProtocol(wikipedia, "http");
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(41, rows.size());
    }

    @Test
    public void isQuery() throws ParseException, MalformedURLException {
        Query q = new Query();
        q.isQuery(wikipedia, "q=abc");
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(0, rows.size());
    }


    // --- Date --------------------------------------------------------------

    @Test
    public void isDate() throws ParseException {
        Date date = tookOffice.parse("20/01/2009");
        Query q = new Query();
        q.is(tookOffice, date);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(1, rows.size());
    }

    @Test
    public void dateGreater() throws ParseException {
        Date date = new GregorianCalendar(2000, 1, 1).getTime();
        Query q = new Query();
        q.isAfter(tookOffice, date);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(2, rows.size());
    }

    @Test
    public void dateLess() throws ParseException {
        Date date = tookOffice.parse("1/1/1800");
        Query q = new Query();
        q.isBefore(tookOffice, date);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(2, rows.size());
    }

    @Test
    public void isWeekday() throws ParseException {
        Query q = new Query();
        q.isWeekday(leftOffice, 10);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(0, rows.size());
    }

    @Test
    public void isWeek() throws ParseException {
        Query q = new Query();
        q.isWeek(leftOffice, 40);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(0, rows.size());
    }

    @Test
    public void isYear() throws ParseException {
        Query q = new Query();
        q.isYear(leftOffice, 1809);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(1, rows.size());
    }

    @Test
    public void isMonth() throws ParseException {
        Query q = new Query();
        q.isMonth(leftOffice, 3);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(3, rows.size());
    }

    @Test
    public void isDayOfMonth() throws ParseException {
        Query q = new Query();
        q.isDayOfMonth(leftOffice, 3);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(0, rows.size());
    }

    @Test
    public void isHour() throws ParseException {
        Query q = new Query();
        q.isHour(leftOffice, -2);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(0, rows.size());
    }

    @Test
    public void isMinute() throws ParseException {
        Query q = new Query();
        q.isMinute(leftOffice, -3);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(0, rows.size());
    }

    @Test
    public void isSecond() throws ParseException {
        Query q = new Query();
        q.isSecond(leftOffice, -5);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(0, rows.size());
    }

    @Test
    public void isMillis() throws ParseException {
        Query q = new Query();
        q.isMillisecond(leftOffice, -1);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals(0, rows.size());
    }

    // --- Sorting --------------------------------------------------------------


    @Test
    public void ascending() throws IOException {
        Query q = new Query();
        q.ascending(leftOffice);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals((Integer) 44, rows.get(0).get(presidency));
    }

    @Test
    public void descending() throws ParseException {
        Query q = new Query();
        q.descending(president);
        List<Row> rows = csv.findRowsByQuery(q);
        assertEquals("Zachary Taylor", rows.get(0).get(president));
    }


    @Test
    public void getSorters() throws IOException {
        Query q = new Query();
        SortOrder so = new SortOrder(presidency, SortDirection.ASC);
        q.addSort(so);
        assertEquals(1, q.getSorters().size());
        q.removeSort(so);
        assertEquals(0, q.getSorters().size());
    }

}
