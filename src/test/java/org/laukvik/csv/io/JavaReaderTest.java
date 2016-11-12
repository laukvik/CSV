package org.laukvik.csv.io;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.Column;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JavaReaderTest {

    @Test
    public void shouldBuildMetaData(){
        List<Column> columns = JavaReader.buildMetaData(Employee.class, new CSV());
        assertEquals(9, columns.size());
    }

    @Test
    public void shouldReadList(){
        List<Employee> list = new ArrayList<>();
        Employee e = new Employee();
        e.stringName = "Bob";
        e.bigDecimalName = new BigDecimal(11);
        e.intName = 12;
        e.floatName = 13f;
        e.booleanName = false;
        e.byteName = 14;
        e.dateName = new Date();
        e.doubleName = 15d;
        try {
            e.urlName = new URL("http://www.google.com");
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        list.add(e);
        Employee e2 = new Employee();
        e2.doubleName = 52d;
        list.add(e2);
        Employee e3 = new Employee();
        e3.floatName = 53f;
        list.add(e3);
        //
        CSV csv = new CSV();
        JavaReader<Employee> reader = new JavaReader<Employee>(list);
        reader.readDataset(csv);
        assertEquals(9, csv.getColumnCount());
    }

    class Employee {
        public String stringName;
        public Integer intName;
        public Float floatName;
        public Boolean booleanName;
        public Byte byteName;
        public Date dateName;
        public Double doubleName;
        public URL urlName;
        public BigDecimal bigDecimalName;
    }

}