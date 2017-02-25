package org.laukvik.csv.fx;

import javafx.scene.control.Tab;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.BigDecimalColumn;
import org.laukvik.csv.query.BigDecimalMatcher;
import org.laukvik.csv.query.BigDecimalRangeMatcher;
import org.laukvik.csv.query.RowMatcher;
import org.laukvik.csv.statistics.BigDecimalDistribution;
import org.laukvik.csv.statistics.BigDecimalRange;
import org.laukvik.csv.statistics.FreqDistribution;
import org.laukvik.csv.statistics.Range;
import org.laukvik.csv.statistics.RangedDistribution;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 *
 */
public class BigDecimalMatcherControl extends ColumnMatcherControl {

    private final Tab uniqueTab;
    private final Tab rangeTab;
    private final FrequencyDistributionTableView uniqueView;
    private final FrequencyDistributionTableView rangeView;

    private final List<RowMatcher> matchers;
    private final Main main;

    public BigDecimalMatcherControl(final BigDecimalColumn column, final Main main) {
        super(column);
        this.main = main;
        matchers = new ArrayList<>();
        uniqueView = new FrequencyDistributionTableView();
        uniqueTab = new Tab("Values", uniqueView);

        rangeView = new FrequencyDistributionTableView();
        rangeTab = new Tab("Range", rangeView);

        // Add tabs
        getTabs().addAll(uniqueTab, rangeTab);
    }

    public List<RowMatcher> getMatchers() {
        BigDecimalColumn bc = (BigDecimalColumn) getColumn();
        matchers.clear();
        List<BigDecimal> bigDecimals;
        bigDecimals = findBigDecimals(uniqueView);
        if (!bigDecimals.isEmpty()) {
            matchers.add(new BigDecimalMatcher(bc, bigDecimals));
        }

        List<BigDecimalRange> rangeList = findBigDecimalRanges(rangeView);
        if (!rangeList.isEmpty()){
            matchers.add(new BigDecimalRangeMatcher(bc, rangeList));
        }

        return matchers;
    }

    public void loadCSV(final CSV csv) {
        uniqueView.getItems().clear();
        BigDecimalColumn sc = (BigDecimalColumn) column;

        // Find range - smallest and largest number
        BigDecimal min = new BigDecimal(0);
        BigDecimal max = new BigDecimal(0);
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            BigDecimal s = r.getBigDecimal(sc);
            if (s != null){
                if (s.compareTo(min) <= 0){
                    min = s;
                }
                if (s.compareTo(max) >= 0){
                    max = s;
                }
            }
        }
        BigDecimalDistribution rangedDistribution = new BigDecimalDistribution();
        rangedDistribution.buildRange(min, max);


        // Find unique distribution
        FreqDistribution<BigDecimal> uniqueDistribution = new FreqDistribution<>(column);
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            BigDecimal s = r.getBigDecimal(sc);
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
        for (BigDecimal key : uniqueDistribution.getKeys()) {
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
