package org.laukvik.csv.report;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ReportTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = ReportTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void addColumn() throws Exception {
        Report r = new Report();
        IntegerColumn c = new IntegerColumn("frequency");
        Count count = new Count(c);
        r.addColumn(count);
    }

    @Test
    public void addGroup() throws Exception {
        Report r = new Report();
        IntegerColumn c = new IntegerColumn("frequency");
        r.addGroup(c);
    }

    @Test
    public void addSort() throws Exception {

    }

    @Test
    public void buildReport() throws Exception {
        CSV csv = new CSV();
        csv.readFile(getResource("metadata.csv"));
        StringColumn homeState = (StringColumn) csv.getColumn("Home state");
        IntegerColumn presidency = (IntegerColumn) csv.getColumn("presidency");
        StringColumn party = (StringColumn) csv.getColumn("Party");

        Report r = new Report();
        r.addGroup(party);
//        r.addGroup(homeState);

//        r.addColumn(new Name(homeState));
        r.addColumn(new Name(party));
//        r.addColumn(new Count(presidency));

//        r.addColumn(new Min(presidency));
//        r.addColumn(new Max(presidency));
        r.addColumn(new Sum(presidency));
//        r.addColumn(new Avg(presidency));

        CSV c = r.buildReport(csv);

        for (int x=0; x<c.getColumnCount(); x++){
            System.out.print( c.getColumn(x).getName() + "\t");
        }
        System.out.println();
        for (int y= 0; y<c.getRowCount(); y++){
            for (int x=0; x<c.getColumnCount(); x++){
                System.out.print( c.getRow(y).get( c.getColumn(x) ) + "\t");
            }
            System.out.println();
        }
    }

    @Test
    public void buildCSV() throws IOException {
        StringColumn land = new StringColumn("land");
        StringColumn by = new StringColumn("by");

        Node root = new Node();
        root.add("Norge",  land).add("Oslo",by);
        root.add("Norge",  land).add("Tromsø",by);
        root.add("Sverige",land).add("Arlanda",by);
        root.add("Norge",  land).add("Bergen",by);
        root.add("Danmark",land).add("København",by);
        root.add("Sverige",land).add("Stockholm",by);

        Report r = new Report();
        CSV csv = r.buildCSV(root);
        assertEquals( 2, csv.getColumnCount() );
        assertEquals( land.getName(), csv.getColumn(0).getName() );
        assertEquals( by.getName(), csv.getColumn(1).getName() );

//        System.out.println("TABELL:");
//        for (int y=0; y < csv.getRowCount(); y++){
//            for (int x=0; x<csv.getColumnCount(); x++){
//                System.out.print( csv.getRow(y).get( csv.getColumn(x)) + "\t");
//            }
//            System.out.println();
//        }

        assertEquals( 6, csv.getRowCount() );

    }

    @Test
    public void buildCSV_node() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("metadata.csv"));
        IntegerColumn presidency = (IntegerColumn) csv.getColumn("presidency");
        StringColumn party = (StringColumn) csv.getColumn("Party");
        StringColumn homeState = (StringColumn) csv.getColumn("Home state");

        Report r = new Report();
        r.addGroup(party);
//        r.addGroup(homeState);

        r.addColumn(new Name(party));
//        r.addColumn(new Name(homeState));
        r.addColumn(new Count(party));
//        r.addColumn(new Count(homeState));

//        r.addMatcher( new IntLessThanMatcher(presidency, 11));
        Node root = r.buildNode(csv);

        CSV csv2 = r.buildCSV(root);
        csv2.writeFile(new File("/Users/morten/Desktop/aggreg.csv"));
    }


}