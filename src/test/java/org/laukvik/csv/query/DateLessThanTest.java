package org.laukvik.csv.query;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateLessThanTest {

    public Date createDate(int year, int month, int day) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    @Test
    public void shouldBeLessThan() {
        Date d1 = createDate(1900, 5, 23);
        Assert.assertEquals(true, DateLessThan.isLessThan(d1, createDate(1900, 5, 24)));
        Assert.assertEquals(true, DateLessThan.isLessThan(d1, createDate(1900, 6, 23)));
        Assert.assertEquals(true, DateLessThan.isLessThan(d1, createDate(1901, 5, 23)));
    }

    @Test
    public void shouldNotBeLessThan() {
        Date d1 = createDate(1900, 5, 23);
        Assert.assertEquals(false, DateLessThan.isLessThan(d1, createDate(1899, 5, 23)));
        Assert.assertEquals(false, DateLessThan.isLessThan(d1, createDate(1899, 6, 23)));
        Assert.assertEquals(false, DateLessThan.isLessThan(d1, createDate(1899, 5, 24)));
    }

}