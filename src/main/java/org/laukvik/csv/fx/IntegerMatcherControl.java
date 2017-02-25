package org.laukvik.csv.fx;

import javafx.scene.control.Tab;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.query.IntIsInMatcher;
import org.laukvik.csv.query.IntegerRangeMatcher;
import org.laukvik.csv.query.RowMatcher;
import org.laukvik.csv.statistics.FreqDistribution;
import org.laukvik.csv.statistics.IntegerDistribution;
import org.laukvik.csv.statistics.IntegerRange;
import org.laukvik.csv.statistics.Range;
import org.laukvik.csv.statistics.RangedDistribution;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 *
 */
public class IntegerMatcherControl extends ColumnMatcherControl {

    private final Tab uniqueTab;
    private final Tab rangeTab;
    private final FrequencyDistributionTableView uniqueView;
    private final FrequencyDistributionTableView rangeView;
    private final List<RowMatcher> matchers;
    private final Main main;

    public IntegerMatcherControl(final IntegerColumn column, final Main main) {
        super(column);
        matchers = new ArrayList<>();
        this.main = main;

        //
        uniqueView = new FrequencyDistributionTableView();
        uniqueTab = new Tab("Values", uniqueView);
        rangeView = new FrequencyDistributionTableView();
        rangeTab = new Tab("Range", rangeView);

        // Add tabs
        getTabs().addAll(uniqueTab, rangeTab);
    }

    public List<RowMatcher> getMatchers() {
        IntegerColumn ic = (IntegerColumn) getColumn();
        matchers.clear();

        List<Integer> intList = findIntegers(uniqueView);
        if (!intList.isEmpty()){
            matchers.add(new IntIsInMatcher(ic, intList));
        }

        List<IntegerRange> rangeList = findIntegerRanges(rangeView);
        if (!rangeList.isEmpty()){
            matchers.add(new IntegerRangeMatcher(ic, rangeList));
        }

        return matchers;
    }


    @Override
    public void loadCSV(final CSV csv) {
        uniqueView.getItems().clear();
        rangeView.getItems().clear();
        IntegerColumn sc = (IntegerColumn) column;

        // Find range - smallest and largest number
        int min = 0;
        int max = 0;
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            Integer s = r.getInteger(sc);
            if (s != null){
                if (s < min){
                    min = s;
                }
                if (s > max){
                    max = s;
                }
            }
        }
        IntegerDistribution rangedDistribution = new IntegerDistribution();
        rangedDistribution.buildRange(min, max);

        // Find unique distribution
        FreqDistribution<Integer> uniqueDistribution = new FreqDistribution<>(column);
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            Integer s = r.getInteger(sc);
            rangedDistribution.addValue(s);
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
        for (Integer key : uniqueDistribution.getKeys()) {
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
