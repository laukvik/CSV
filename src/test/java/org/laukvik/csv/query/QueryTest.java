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

import org.junit.Assert;
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


public class QueryTest {

    CSV csv;
    SimpleDateFormat format;

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
            for (int x = 0; x < csv.getColumnCount(); x++) {
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void readMetaData() {
        IntegerColumn presidency = (IntegerColumn) csv.getColumn("Presidency");
        DateColumn tookOffice = (DateColumn) csv.getColumn("Took office");
        UrlColumn wikipedia = (UrlColumn) csv.getColumn("Wikipedia Entry");
        Assert.assertNotNull(presidency);
    }

    @Test
    public void is() {
        IntegerColumn presidency = (IntegerColumn) csv.getColumn("Presidency");
        List<Row> rows = csv.findByQuery().where().column(presidency).is(10).getResultList();
        Assert.assertEquals("Only one item", 1, rows.size());
    }

    @Test
    public void between() {
        IntegerColumn presidency = (IntegerColumn) csv.getColumn("Presidency");
        List<Row> rows = csv.findByQuery().where().column(presidency).isBetween(10, 19).getResultList();
        Assert.assertEquals("Should be 10", 10, rows.size());
    }

    @Test
    public void isGreaterThan() {
        IntegerColumn presidency = (IntegerColumn) csv.getColumn("Presidency");
        List<Row> rows = csv.findByQuery().where().column(presidency).isGreaterThan(40).getResultList();
        Assert.assertEquals("Should be 4", 4, rows.size());
    }

    @Test
    public void isLessThan() {
        IntegerColumn presidency = (IntegerColumn) csv.getColumn("Presidency");
        List<Row> rows = csv.findByQuery().where().column(presidency).isLessThan(41).getResultList();
        Assert.assertEquals("Should be 40", 40, rows.size());
    }

    @Test
    public void isIn() {
        IntegerColumn presidency = (IntegerColumn) csv.getColumn("Presidency");
        List<Row> rows = csv.findByQuery().where().column(presidency).isIn(1, 3, 5).getResultList();
        Assert.assertEquals("Should be 3", 3, rows.size());
    }

    @Test
    public void isDate() throws ParseException {
        DateColumn tookOffice = (DateColumn) csv.getColumn("Took office");
        Date date = tookOffice.parse("20/01/2009");
        List<Row> rows = csv.findByQuery().where().column(tookOffice).isDate(date).getResultList();
        Assert.assertEquals("Should be 1", 1, rows.size());
    }

    @Test
    public void isDateGreater() throws ParseException {
        Date date = new GregorianCalendar(2000, 1, 1).getTime();
        DateColumn tookOffice = (DateColumn) csv.getColumn("Took office");
        List<Row> rows = csv.findByQuery().where().column(tookOffice).isDateGreaterThan(date).getResultList();
        Assert.assertEquals("Should be 2", 2, rows.size());
    }

    @Test
    public void isDateLess() throws ParseException {
        String to = "1/1/1800";
        DateColumn tookOffice = (DateColumn) csv.getColumn("Took office");
        Date date = tookOffice.parse(to);
        List<Row> rows = csv.findByQuery().where().column(tookOffice).isDateLessThan(date).getResultList();
        Assert.assertEquals("Should be 2", 2, rows.size());
    }

    @Test
    public void sortDesc() throws ParseException {
        StringColumn president = (StringColumn) csv.getColumn("President");
        List<Row> rows = csv.findByQuery().orderBy().desc(president).getResultList();
        Assert.assertEquals("Should be Zachary Taylor", "Zachary Taylor", rows.get(0).getString(president));
    }

    @Test
    public void sortAsc() throws ParseException, IOException {
        csv.readFile(getResource("presidents.csv"));
        StringColumn president = (StringColumn) csv.getColumn("President");
        List<Row> rows = csv.findByQuery().orderBy().asc(president).getResultList();
        Assert.assertEquals("Should be Abraham Lincoln", "Abraham Lincoln", rows.get(0).getString(president));
    }

    @Test
    public void sortDate() throws ParseException {
        StringColumn president = (StringColumn) csv.getColumn("President");
        DateColumn tookOffice = (DateColumn) csv.getColumn("Took office");
        List<Row> rows = csv.findByQuery().orderBy().desc(tookOffice).getResultList();
        Assert.assertEquals("Should be Barack Obama", "Barack Obama", rows.get(0).getString(president));
    }

    @Test
    public void usingWhere() throws ParseException {
        StringColumn homeState = (StringColumn) csv.getColumn("Home State");
        StringColumn president = (StringColumn) csv.getColumn("President");
        List<Row> rows = csv.findByQuery().where().column(homeState).is("Virginia").orderBy().asc(president).getResultList();
        Assert.assertEquals("Should find 5", 5, rows.size());
    }

    @Test
    public void isEmpty() throws ParseException {
        DateColumn leftOffice = (DateColumn) csv.getColumn("Left office");
        List<Row> rows = csv.findByQuery().where().column(leftOffice).isEmpty().getResultList();
        Assert.assertEquals("Should find 1 empty", 1, rows.size());
    }

    @Test
    public void isYear() throws ParseException {
        DateColumn leftOffice = (DateColumn) csv.getColumn("Left office");
        List<Row> rows = csv.findByQuery().where().column(leftOffice).isYear(1809).getResultList();
        Assert.assertEquals("Should find 1", 1, rows.size());
    }

    @Test
    public void findWithQueries() throws IOException, ParseException {
        StringColumn homeState = (StringColumn) csv.getColumn("Home State");
        StringColumn party = (StringColumn) csv.getColumn("Party");
        List<Row> rows;
        rows = csv.findByQuery().where().column(homeState).is("New York").getResultList();
        Assert.assertEquals(7, rows.size());
        rows = csv.findByQuery().where().column(party).is("Democratic").getResultList();
        Assert.assertEquals(15, rows.size());
        rows = csv.findByQuery().where().column(homeState).is("New York").column(party).is("Democratic").getResultList();
        Assert.assertEquals(4, rows.size());
    }

    @Test
    public void test() {
        CSV csv = new CSV();
        Query q = csv.findByQuery();
    }

}
