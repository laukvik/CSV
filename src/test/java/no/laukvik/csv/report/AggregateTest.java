package no.laukvik.csv.report;

import no.laukvik.csv.columns.IntegerColumn;
import no.laukvik.csv.columns.StringColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AggregateTest {

    final IntegerColumn ic = new IntegerColumn("int");
    final Count count = new Count(ic);

    @Test
    public void getColumn() throws Exception {
        assertEquals(ic, count.getColumn());
    }

    @Test
    public void aggregate() throws Exception {
    }

    @Test
    public void getValue() throws Exception {
    }

    @Test
    public void aggregateColumnProperty() throws Exception {
        StringColumn sc = new StringColumn("a");
        count.setAggregateColumn(sc);
        assertEquals(sc, count.getAggregateColumn());
    }

}