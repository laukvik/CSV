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

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.CSVTest;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.columns.UrlColumn;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class QueryTest {

    CSV csv;
    SimpleDateFormat format;

    public static File getResource(String filename) {
        ClassLoader classLoader = CSVTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Before
    public void setup() {
        format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            // Presidency,President,Wikipedia Entry,Took office,Left office,Party,Portrait,Thumbnail,Home State
            /*
             * 1,
             * George Washington,
             * http://en.wikipedia.org/wiki/George_Washington,
             * 30/04/1789,
             * 4/03/1797,
             *  Independent ,
             * GeorgeWashington.jpg,
             * thmb_GeorgeWashington.jpg,
             * Virginia */
            MetaData md = new MetaData();
            md.addColumn(new IntegerColumn());
            md.addColumn(new StringColumn());
            md.addColumn(new UrlColumn());
            md.addColumn(new DateColumn(format));
            md.addColumn(new DateColumn(format));
            md.addColumn(new StringColumn());
            md.addColumn(new StringColumn());
            md.addColumn(new StringColumn());
            md.addColumn(new StringColumn());

            csv = new CSV(getResource("presidents.csv"), md);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void is() {
        List<Row> rows = csv.findByQuery().where().column("Presidency").is(10).getResultList();
//        for (Row r : rows) {
//            System.out.println(r);
//        }
        Assert.assertEquals("Only one item", 1, rows.size());
    }

    @Test
    public void between() {
        List<Row> rows = csv.findByQuery().where().column("Presidency").isBetween(10, 19).getResultList();
//        for (Row r : rows) {
//            System.out.println(r);
//        }
        Assert.assertEquals("Should be 10", 10, rows.size());
    }

    @Test
    public void isGreaterThan() {
        List<Row> rows = csv.findByQuery().where().column("Presidency").isGreaterThan(40).getResultList();
//        for (Row r : rows) {
//            System.out.println(r);
//        }
        Assert.assertEquals("Should be 4", 4, rows.size());
    }

    @Test
    public void isLessThan() {
        List<Row> rows = csv.findByQuery().where().column("Presidency").isLessThan(41).getResultList();
//        for (Row r : rows) {
//            System.out.println(r);
//        }
        Assert.assertEquals("Should be 40", 40, rows.size());
    }

    @Test
    public void isIn() {
        List<Row> rows = csv.findByQuery().where().column("Presidency").isIn(1, 3, 5).getResultList();
//        for (Row r : rows) {
//            System.out.println(r);
//        }
        Assert.assertEquals("Should be 3", 3, rows.size());
    }

    @Test
    public void isDate() throws ParseException {
        String to = "20/01/2009";
        Date tookOffice = format.parse(to);

        List<Row> rows = csv.findByQuery().where().column("Took office").isDate(tookOffice, format).getResultList();
//        for (Row r : rows) {
//            System.out.println(r);
//        }
        Assert.assertEquals("Should be 1", 1, rows.size());
    }

    @Test
    public void isDateGreater() throws ParseException {
        String to = "1/1/2000";

        Date tookOffice = format.parse(to);
        List<Row> rows = csv.findByQuery().where().column("Took office").isDateGreaterThan(tookOffice, format).getResultList();
        Assert.assertEquals("Should be 2", 2, rows.size());
    }

    @Test
    public void isDateLess() throws ParseException {
        String to = "1/1/1800";
        Date tookOffice = format.parse(to);

        List<Row> rows = csv.findByQuery().where().column("Took office").isDateLessThan(tookOffice, format).getResultList();
//        for (Row r : rows) {
//            System.out.println(r);
//        }
        Assert.assertEquals("Should be 2", 2, rows.size());
    }

    @Test
    public void sortDesc() throws ParseException {
        List<Row> rows = csv.findByQuery().orderBy().desc("President").getResultList();
        for (Row r : rows) {
//            System.out.println(r.getString("President"));
        }
        Assert.assertEquals("Should be Zachary Taylor", "Zachary Taylor", rows.get(0).getString("President"));
    }

    @Test
    public void sortAsc() throws ParseException {
        List<Row> rows = csv.findByQuery().orderBy().asc("President").getResultList();
        for (Row r : rows) {
//            System.out.println(r.getString("President"));
        }
        Assert.assertEquals("Should be Abraham Lincoln", "Abraham Lincoln", rows.get(0).getString("President"));
    }

    @Test
    public void sortDate() throws ParseException {
        List<Row> rows = csv.findByQuery().orderBy().desc("Took office").getResultList();
        for (Row r : rows) {
//            System.out.println(r.getValue("Took office") + " " + r.getString("President"));
        }
        Assert.assertEquals("Should be Barack Obama", "Barack Obama", rows.get(0).getString("President"));
    }

    @Test
    public void usingWhere() throws ParseException {
        List<Row> rows = csv.findByQuery().where().column("Home State").is("Virginia").orderBy().asc("President").getResultList();
        for (Row r : rows) {
//            System.out.println(r.getString("President") + " " + r.getString("Home State"));
        }
        Assert.assertEquals("Should find 5", 5, rows.size());
    }

    @Test
    public void isEmpty() throws ParseException {
        List<Row> rows = csv.findByQuery().where().column("Left office").isEmpty().getResultList();
        for (Row r : rows) {
//            System.out.println(r.getString("President") + " " + r.getString("Home State"));
        }
        Assert.assertEquals("Should find 1 empty", 1, rows.size());
    }

    @Test
    public void usingWhereYear() throws ParseException {
        List<Row> rows = csv.findByQuery().where().column("Took office").isYear(1901).getResultList();
        for (Row r : rows) {
//            System.out.println(r.getString("President") + " " + r.getString("Home State"));
        }
        Assert.assertEquals("Should find 1", 1, rows.size());
    }

}
