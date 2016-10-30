package org.laukvik.csv.query;

import org.junit.Assert;
import org.junit.Test;
import org.laukvik.csv.columns.DateColumn;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Morten Laukvik
 */
public class DateIsMatcherTest {

    public Date createDate(int year, int month, int day) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    @Test
    public void isEqual() throws Exception {
        Date d1 = createDate(1951, 3, 22);
        Date d2 = createDate(1951, 3, 22);
        Assert.assertTrue(DateColumn.isEqualDate(d1, d2));
    }

    @Test
    public void isEqualFail() throws Exception {
        Date d1 = createDate(1951, 3, 21);
        Date d2 = createDate(1951, 3, 22);
        Assert.assertTrue(!DateColumn.isEqualDate(d1, d2));
    }

}