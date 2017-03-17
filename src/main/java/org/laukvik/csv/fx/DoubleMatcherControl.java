package org.laukvik.csv.fx;

import javafx.scene.control.Tab;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DoubleColumn;
import org.laukvik.csv.query.DoubleMatcher;
import org.laukvik.csv.query.DoubleRangeMatcher;
import org.laukvik.csv.query.RowMatcher;
import org.laukvik.csv.statistics.DoubleDistribution;
import org.laukvik.csv.statistics.DoubleRange;
import org.laukvik.csv.statistics.FreqDistribution;
import org.laukvik.csv.statistics.Range;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 *
 */
public class DoubleMatcherControl extends ColumnMatcherControl {

    private final FrequencyDistributionTableView uniqueView;
    private final FrequencyDistributionTableView rangeView;
    private final List<RowMatcher> matchers;

    private final Main main;

    public DoubleMatcherControl(final DoubleColumn column, final Main main) {
        super(column);
        this.main = main;
        matchers = new ArrayList<>();

        //
        uniqueView = new FrequencyDistributionTableView();
        Tab uniqueTab = new Tab("Values", uniqueView);
        rangeView = new FrequencyDistributionTableView();
        Tab rangeTab = new Tab("Range", rangeView);

        // Add tabs
        getTabs().addAll(uniqueTab, rangeTab);
    }

    public List<RowMatcher> getMatchers() {
        DoubleColumn ic = (DoubleColumn) getColumn();
        matchers.clear();

        List<Double> doubleList = findDoubles(uniqueView);
        if (!doubleList.isEmpty()) {
            matchers.add(new DoubleMatcher(ic, doubleList));
        }

        List<DoubleRange> rangeList = findDoubleRanges(rangeView);
        if (!rangeList.isEmpty()){
            matchers.add(new DoubleRangeMatcher(ic, rangeList));
        }

        return matchers;
    }

    public void loadCSV(final CSV csv) {
        uniqueView.getItems().clear();
        DoubleColumn sc = (DoubleColumn) column;

        // Find range - smallest and largest number
        double min = 0;
        double max = 0;
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            Double s = r.getDouble(sc);
            if (s != null){
                if (s < min){
                    min = s;
                }
                if (s > max){
                    max = s;
                }
            }
        }
        DoubleDistribution rangedDistribution = new DoubleDistribution();
        rangedDistribution.buildRange(min, max);

        // Find unique distribution
        FreqDistribution<Double> uniqueDistribution = new FreqDistribution<>(column);
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            Double s = r.getDouble(sc);
            if (s == null) {
                uniqueDistribution.addValue(null);
            } else {
                uniqueDistribution.addValue(s);
            }
        }

        // Unique
        if (uniqueDistribution.getNullCount() > 0) {
            uniqueView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, uniqueDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Double key : uniqueDistribution.getKeys()) {
            uniqueView.getItems().add(new ObservableFrequencyDistribution(false,
                    key + "", key, uniqueDistribution.getCount(key),
                    getColumn(),
                    main));
        }
        // Range
        if (rangedDistribution.getNullCount() > 0) {
            rangeView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, rangedDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Range r : rangedDistribution.getRanges()){
            rangeView.getItems().add(new ObservableFrequencyDistribution(false,
                    r.getLabel(), r, r.getCount(),
                    getColumn(),
                    main));
        }
    }

}
