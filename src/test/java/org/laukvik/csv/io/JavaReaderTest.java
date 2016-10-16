package org.laukvik.csv.io;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JavaReaderTest {

    class Employee{
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

    @Test
    public void shouldBuildMetaData(){
        MetaData md = JavaReader.buildMetaData(Employee.class);
        for (int x=0; x<md.getColumnCount(); x++){
//            System.out.println(x + "\t" + md.getColumn(x).getName());
        }
        assertEquals(9, md.getColumnCount());
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
        JavaReader<Employee> reader = new JavaReader<Employee>( csv, list );
        MetaData md = reader.getMetaData();
        assertEquals(9, md.getColumnCount());
        int y = 0;
        while(reader.hasNext()){
            Row r = reader.next();
            for (int x=0; x<md.getColumnCount(); x++){
                Column c = md.getColumn(x);
            }
            y++;
        }
    }

}