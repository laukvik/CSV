package org.laukvik.csv;

import org.junit.Test;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.columns.UrlColumn;
import org.laukvik.csv.query.Query;
import org.laukvik.csv.statistics.FrequencyDistribution;
import org.laukvik.csv.statistics.IntegerDistribution;
import org.laukvik.csv.statistics.IntegerRange;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Examples {

    @Test
    public void frequencyDistribution() {
        StringColumn c = new StringColumn("president");
        FrequencyDistribution<String> distribution = new FrequencyDistribution(c);
        distribution.addValue("Barack Obama");
        distribution.addValue("Donald Trump");
        distribution.addValue(null);
        distribution.addValue("Barack Obama");
        for (String key : distribution.getKeys()) {
            //System.out.println(key + ": " + distribution.getCount(key));
        }
    }

    @Test
    public void rangedDistribution() {
        IntegerDistribution distribution = new IntegerDistribution();
        distribution.addRange(new IntegerRange("Q1", 0, 25));
        distribution.addRange(new IntegerRange("Q2", 25, 50));
        distribution.addRange(new IntegerRange("Q3", 50, 75));
        distribution.addRange(new IntegerRange("Q4", 75, 100));

        distribution.addValue(0);
        distribution.addValue(24);
        distribution.addValue(25);
        distribution.addValue(44);
        distribution.addValue(55);
        distribution.addValue(110);
        distribution.addValue(null);
        distribution.addValue(null);

        for (IntegerRange r : distribution.getRanges()) {
            //System.out.println(r.getLabel() + ": " + r.getCount());
        }
    }

    @Test
    public void query() throws MalformedURLException {
        // Build an empty CSV with a few columns
        CSV csv = new CSV();
        StringColumn president = csv.addStringColumn("President");
        StringColumn party = csv.addStringColumn("Party");
        IntegerColumn presidency = csv.addIntegerColumn("Presidency");
        UrlColumn web = csv.addUrlColumn("web");
        DateColumn tookOffice = csv.addDateColumn("Took office", "dd/MM/yyyy");

        // Add test values
        csv.addRow()
                .set(president, "Donald Trump")
                .set(presidency, 45)
                .set(party, null)
                .set(tookOffice, tookOffice.parse("20/01/2017"))
                .set(web, new URL("https://en.wikipedia.org/wiki/Donald_Trump"));
        csv.addRow()
                .set(president, "Barack Obama")
                .set(presidency, 44)
                .set(party, "Democratic")
                .set(tookOffice, tookOffice.parse("20/01/2009"))
                .set(web, new URL("http://en.wikipedia.org/wiki/Barack_Obama"));
        csv.addRow()
                .set(president, "George W. Bush")
                .set(presidency, 43)
                .set(party, "Republican")
                .set(tookOffice, tookOffice.parse("20/01/2001"))
                .set(web, new URL("http://en.wikipedia.org/wiki/George_W._Bush"));
        csv.addRow()
                .set(president, "Bill Clinton")
                .set(presidency, 42)
                .set(party, "Democratic")
                .set(tookOffice, tookOffice.parse("20/01/1993"))
                .set(web, new URL("http://en.wikipedia.org/wiki/Bill_Clinton"));
        csv.addRow()
                .set(president, "George H. W. Bush")
                .set(presidency, 41)
                .set(party, "Republican")
                .set(tookOffice, tookOffice.parse("20/01/1989"))
                .set(web, new URL("http://en.wikipedia.org/wiki/George_H._W._Bush"));
        csv.addRow()
                .set(president, "Ronald Reagan")
                .set(presidency, 40)
                .set(party, "Republican")
                .set(tookOffice, tookOffice.parse("20/01/1981"))
                .set(web, new URL("http://en.wikipedia.org/wiki/Ronald_Reagan"));

        // Build the query
        Query query = new Query()
                .isBetween(presidency, 41, 45)
                .isDayOfMonth(tookOffice, 20)
                .isWordCount(president, 2, 3, 4)
                .isAfter(tookOffice, tookOffice.parse("01/01/1999"))
                .isYear(tookOffice, 2009, 2017)
                .isEmpty(party)
                .isNotEmpty(web)
                .ascending(presidency);
        List<Row> rows = csv.findRowsByQuery(query);

        // Display the results
        for (Row r : rows) {
            System.out.println(r.get(presidency) + ": " + r.get(president));
        }

    }

}
