package org.laukvik.csv.fx;

import org.junit.Assert;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.ParseException;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.IOException;

/**
 * @author Morten Laukvik
 */
public class QueryModelTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = QueryModelTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    public CSV findCSV() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("metadata.csv"));
        return csv;
    }

    @Test
    public void shouldAddSelection() throws IOException, ParseException {
        CSV csv = findCSV();
        StringColumn homeState = (StringColumn) csv.getMetaData().getColumn("Home State");
        StringColumn party = (StringColumn) csv.getMetaData().getColumn("Party");
        QueryModel model = new QueryModel(csv, null);
        model.addSelection(homeState, "New York");
        Assert.assertNotNull(model.findSelectionByColumn(homeState));
        model.removeSelection(homeState, "New York");
        Assert.assertNull(model.findSelectionByColumn(homeState));
        model.addSelection(homeState, "New York");
        model.addSelection(homeState, "California");
    }

}