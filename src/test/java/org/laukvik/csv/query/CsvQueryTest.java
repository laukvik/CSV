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
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CsvQueryTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvQueryTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    public CSV findCSV() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("metadata.csv"));
        return csv;
    }

    @Test
    public void lessThan() throws IOException {
        CSV csv = findCSV();
        IntegerColumn presidency = (IntegerColumn) csv.getMetaData().getColumn("Presidency");
        List<Row> rows = csv.findByQuery().where().column(presidency).isLessThan(11).getResultList();
        Assert.assertEquals(10, rows.size());
    }

    @Test
    public void greaterThan() throws IOException {
        CSV csv = findCSV();
        IntegerColumn presidency = (IntegerColumn) csv.getMetaData().getColumn("Presidency");
        List<Row> rows = csv.findByQuery().where().column(presidency).isGreaterThan(40).getResultList();
        Assert.assertEquals(4, rows.size());
    }

    @Test
    public void getResultList() throws IOException {
        CSV csv = findCSV();
        IntegerColumn presidency = (IntegerColumn) csv.getMetaData().getColumn("Presidency");
        List<Row> rows = csv.findByQuery().where().getResultList();
        Assert.assertEquals(44, rows.size());
    }

    @Test
    public void intIs() throws IOException {
        CSV csv = findCSV();
        IntegerColumn presidency = (IntegerColumn) csv.getMetaData().getColumn("Presidency");
        List<Row> rows = csv.findByQuery().where().column(presidency).is(5).getResultList();
        Assert.assertEquals(1, rows.size());
    }

    @Test
    public void isBetween() throws IOException {
        CSV csv = findCSV();
        IntegerColumn presidency = (IntegerColumn) csv.getMetaData().getColumn("Presidency");
        List<Row> rows = csv.findByQuery().where().column(presidency).isBetween(10, 12).getResultList();
        Assert.assertEquals(3, rows.size());
    }

    @Test
    public void isEmpty() throws IOException {
        CSV csv = findCSV();
        IntegerColumn presidency = (IntegerColumn) csv.getMetaData().getColumn("Presidency");
        List<Row> rows = csv.findByQuery().where().column(presidency).isEmpty().getResultList();
        Assert.assertEquals(0, rows.size());
    }

    @Test
    public void isIn() throws IOException {
        CSV csv = findCSV();
        IntegerColumn presidency = (IntegerColumn) csv.getMetaData().getColumn("Presidency");
        List<Row> rows = csv.findByQuery().where().column(presidency).isIn(1, 2, 3).getResultList();
        Assert.assertEquals(3, rows.size());
    }

    @Test
    public void stringIs() throws IOException {
        CSV csv = findCSV();
        StringColumn party = (StringColumn) csv.getMetaData().getColumn("Party");
        List<Row> rows = csv.findByQuery().where().column(party).is("Whig").getResultList();
        Assert.assertEquals(4, rows.size());
    }

    @Test
    public void stringIsIn() throws IOException {
        CSV csv = findCSV();
        StringColumn party = (StringColumn) csv.getMetaData().getColumn("Party");
        List<Row> rows = csv.findByQuery().where().column(party).isIn("Whig", "Independent").getResultList();
//        Assert.assertEquals( 5, rows.size() );
    }

    @Test
    public void stringEmpty() throws IOException {
        CSV csv = findCSV();
        StringColumn party = (StringColumn) csv.getMetaData().getColumn("Party");
        List<Row> rows = csv.findByQuery().where().column(party).isEmpty().getResultList();
        Assert.assertEquals(0, rows.size());
    }

    @Test
    public void stringNotEmpty() throws IOException {
        CSV csv = findCSV();
        StringColumn party = (StringColumn) csv.getMetaData().getColumn("Party");
        List<Row> rows = csv.findByQuery().where().column(party).isNotEmpty().getResultList();
        Assert.assertEquals(44, rows.size());
    }

    public Date createDate(int year, int month, int day) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    @Test
    public void isDate() throws IOException {
        CSV csv = findCSV();
        DateColumn tookOffice = (DateColumn) csv.getMetaData().getColumn("Took office");
        List<Row> rows = csv.findByQuery().where().column(tookOffice).isDate(createDate(1789, 3, 30)).getResultList();
        Assert.assertEquals(1, rows.size());
    }

    @Test
    public void isDateGreater() throws IOException {
        CSV csv = findCSV();
        DateColumn tookOffice = (DateColumn) csv.getMetaData().getColumn("Took office");
        List<Row> rows = csv.findByQuery().where().column(tookOffice).isDateGreaterThan(createDate(1900, 1, 1)).getResultList();
        Assert.assertEquals(19, rows.size());
    }

    @Test
    public void isDateLess() throws IOException {
        CSV csv = findCSV();
        DateColumn tookOffice = (DateColumn) csv.getMetaData().getColumn("Took office");
        List<Row> rows = csv.findByQuery().where().column(tookOffice).isDateLessThan(createDate(1900, 1, 1)).getResultList();
        Assert.assertEquals(25, rows.size());
    }

}
