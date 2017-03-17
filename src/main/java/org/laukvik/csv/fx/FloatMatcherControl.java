package org.laukvik.csv.fx;

import javafx.scene.control.Tab;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.query.FloatMatcher;
import org.laukvik.csv.query.FloatRangeMatcher;
import org.laukvik.csv.query.RowMatcher;
import org.laukvik.csv.statistics.FloatDistribution;
import org.laukvik.csv.statistics.FloatRange;
import org.laukvik.csv.statistics.FreqDistribution;
import org.laukvik.csv.statistics.Range;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 *
 */
public class FloatMatcherControl extends ColumnMatcherControl {

    private final FrequencyDistributionTableView uniqueView;
    private final FrequencyDistributionTableView rangeView;
    private final List<RowMatcher> matchers;
    private final Main main;

    public FloatMatcherControl(final FloatColumn column, final Main main) {
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
        FloatColumn fc = (FloatColumn) getColumn();
        matchers.clear();

        List<Float> floatList = findFloats(uniqueView);
        if (!floatList.isEmpty()) {
            matchers.add(new FloatMatcher(fc, floatList));
        }

        List<FloatRange> rangeList = findFloatRanges(rangeView);
        if (!rangeList.isEmpty()){
            matchers.add(new FloatRangeMatcher(fc, rangeList));
        }

        return matchers;
    }

    public void loadCSV(final CSV csv) {
        uniqueView.getItems().clear();
        FloatColumn sc = (FloatColumn) column;

        // Find range - smallest and largest number
        float min = 0;
        float max = 0;
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            Float s = r.getFloat(sc);
            if (s != null){
                if (s < min){
                    min = s;
                }
                if (s > max){
                    max = s;
                }
            }
        }
        FloatDistribution rangedDistribution = new FloatDistribution();
        rangedDistribution.buildRange(min, max);


        // Find unique distribution
        FreqDistribution<Float> uniqueDistribution = new FreqDistribution<>(column);
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            Float s = r.getFloat(sc);
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
        for (Float key : uniqueDistribution.getKeys()) {
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
