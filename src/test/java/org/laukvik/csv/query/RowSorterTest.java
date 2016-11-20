package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 */
public class RowSorterTest {

    @Test
    public void compare() throws Exception {
        CSV csv = new CSV();
        IntegerColumn created = csv.addIntegerColumn("nr");
        StringColumn first = csv.addStringColumn("first");

        List<SortOrder> order1 = new ArrayList<>();
        order1.add( new SortOrder(first, SortOrder.ASC) );

        RowSorter sort1 = new RowSorter(order1);

        Row r1 = csv.addRow().setInteger(created, 2).setString(first, "Steve");
        Row r2 = csv.addRow().setInteger(created, 3).setString(first, "Bill");
        Row r3 = csv.addRow().setInteger(created, 1).setString(first, "Unknown");
        Row r4 = csv.addRow().setInteger(created, 1).setString(first, "Unknown");

        assertTrue(sort1.compare(r1,r3) < 0);
        assertTrue(sort1.compare(r3,r1) > 0);
        assertTrue(sort1.compare(r4,r4) == 0);

    }

}