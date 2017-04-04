package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.IntegerColumn;
import no.laukvik.csv.columns.StringColumn;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class RowSorterTest {

    @Test
    public void compare() throws Exception {
        CSV csv = new CSV();
        IntegerColumn created = csv.addIntegerColumn("nr");
        StringColumn first = csv.addStringColumn("first");

        List<SortOrder> order1 = new ArrayList<>();
        order1.add(new SortOrder(first, SortOrder.ASC));

        RowSorter sort1 = new RowSorter(order1);

        Row r1 = csv.addRow().set(created, 2).set(first, "Steve");
        Row r2 = csv.addRow().set(created, 3).set(first, "Bill");
        Row r3 = csv.addRow().set(created, 1).set(first, "Unknown");
        Row r4 = csv.addRow().set(created, 1).set(first, "Unknown");

        assertTrue(sort1.compare(r1, r3) < 0);
        assertTrue(sort1.compare(r3, r1) > 0);
        assertTrue(sort1.compare(r4, r4) == 0);
    }

}