package org.laukvik.csv.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.query.FirstLetterMatcher;
import org.laukvik.csv.query.PostfixMatcher;
import org.laukvik.csv.query.PrefixMatcher;
import org.laukvik.csv.query.RowMatcher;
import org.laukvik.csv.query.StringInMatcher;
import org.laukvik.csv.query.StringLengthMatcher;
import org.laukvik.csv.query.WordCountMatcher;
import org.laukvik.csv.statistics.FreqDistribution;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class StringMatcherControl extends ColumnMatcherControl {

    private final Tab uniqueTab;
    private final Tab firstLetterTab;
    private final Tab lengthTab;
    private final Tab prefixTab;
    private final Tab postfixTab;
    private final Tab wordsTab;

    private final FrequencyDistributionTableView uniqueView;
    private final FrequencyDistributionTableView firstLetterView;
    private final FrequencyDistributionTableView lengthView;
    private final FrequencyDistributionTableView prefixView;
    private final FrequencyDistributionTableView postfixView;
    private final FrequencyDistributionTableView wordsView;
    private final Main main;

    private final List<RowMatcher> matchers;

    public StringMatcherControl(final StringColumn column, final Main main) {
        super(column);
        setSide(Side.BOTTOM);
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        this.main = main;
        uniqueView = new FrequencyDistributionTableView();
        uniqueTab = new Tab("Values");
        uniqueTab.setContent(uniqueView);
        firstLetterView = new FrequencyDistributionTableView();
        firstLetterTab = new Tab("Letter");
        firstLetterTab.setContent(firstLetterView);
        lengthView = new FrequencyDistributionTableView();
        lengthTab = new Tab("Length");
        lengthTab.setContent(lengthView);
        prefixView = new FrequencyDistributionTableView();
        prefixTab = new Tab("Prefix");
        prefixTab.setContent(prefixView);
        postfixView = new FrequencyDistributionTableView();
        postfixTab = new Tab("Postfix");
        postfixTab.setContent(postfixView);
        wordsView = new FrequencyDistributionTableView();
        wordsTab = new Tab("Words");
        wordsTab.setContent(wordsView);
        matchers = new ArrayList<>();
        getTabs().addAll(uniqueTab, firstLetterTab, prefixTab, postfixTab, lengthTab, wordsTab);
    }

//    public FrequencyDistributionTableView getTableView(int index){
//        switch (index){
//            case 0 : return uniqueView;
//            case 1 : return firstLetterView;
//            case 2 : return lengthView;
//            case 3 : return prefixView;
//            case 4 : return postfixView;
//            case 5 : return wordsView;
//            default : return null;
//        }
//    }

//    @Override
//    public PieChart buildPieChart(final ColumnMatcherControl columnMatcherControl) {
//        getSelectionModel().getSelectedIndex();
//        List<PieChart.Data> dataset = new ArrayList<>();
//        int max = uniqueView.getItems().size();
//        for (int y= 0; y< max; y++){
//            ObservableFrequencyDistribution ofd = uniqueView.getItems().get(y);
//            dataset.add(new PieChart.Data(ofd.labelProperty().getName(), ofd.countProperty().intValue()));
//        }
//        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(dataset);
//        return new PieChart(data);
//    }

    public List<RowMatcher> getMatchers() {
        StringColumn sc = (StringColumn) getColumn();
        matchers.clear();

        List<String> stringList;
        List<Integer> intList;

        stringList = findStrings(uniqueView);
        if (!stringList.isEmpty()){
            matchers.add(new StringInMatcher(sc, stringList));
        }

        stringList = findStrings(firstLetterView);
        if (!stringList.isEmpty()){
            matchers.add(new FirstLetterMatcher(sc, stringList));
        }

        intList = findIntegers(lengthView);
        if (!intList.isEmpty()){
            matchers.add(new StringLengthMatcher(sc, intList));
        }

        stringList = findStrings(prefixView);
        if (!stringList.isEmpty()){
            matchers.add(new PrefixMatcher(sc, stringList));
        }

        stringList = findStrings(postfixView);
        if (!stringList.isEmpty()){
            matchers.add(new PostfixMatcher(sc, stringList));
        }

        intList = findIntegers(wordsView);
        if (!intList.isEmpty()){
            matchers.add(new WordCountMatcher(sc, intList));
        }

        return matchers;
    }

    @Override
    public void loadCSV(final CSV csv) {
        uniqueView.getItems().clear();
        firstLetterView.getItems().clear();
        lengthView.getItems().clear();
        prefixView.getItems().clear();
        postfixView.getItems().clear();
        wordsView.getItems().clear();


        StringColumn sc = (StringColumn) column;

        FreqDistribution<String> uniqueDistribution = new FreqDistribution<>(column);
        FreqDistribution<String> firstLetterDistribution = new FreqDistribution<>(column);
        FreqDistribution<Integer> lengthDistribution = new FreqDistribution<>(column);
        FreqDistribution<String> prefixDistribution = new FreqDistribution<>(column);
        FreqDistribution<String> postfixDistribution = new FreqDistribution<>(column);
        FreqDistribution<Integer> wordsDistribution = new FreqDistribution<>(column);

        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            String s = r.getString(sc);
            if (s == null) {
                uniqueDistribution.addValue(null);
                firstLetterDistribution.addValue(null);
                prefixDistribution.addValue(null);
                lengthDistribution.addValue(null);
                postfixDistribution.addValue(null);
                wordsDistribution.addValue(null);
            } else {
                uniqueDistribution.addValue(s);
                firstLetterDistribution.addValue(StringColumn.getFirstLetter(s));
                lengthDistribution.addValue(s.length());
                prefixDistribution.addValue(StringColumn.getPrefix(s));
                postfixDistribution.addValue(StringColumn.getPostfix(s));
                wordsDistribution.addValue(StringColumn.getWordCount(s));
            }
        }

        // Unique
        if (uniqueDistribution.getNullCount() > 0) {
            uniqueView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, uniqueDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (String key : uniqueDistribution.getKeys()) {
            uniqueView.getItems().add(new ObservableFrequencyDistribution(false,
                    key, key, uniqueDistribution.getCount(key),
                    getColumn(),
                    main));
        }
        // FirstLetter
        if (firstLetterDistribution.getNullCount() > 0) {
            firstLetterView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, firstLetterDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (String key : firstLetterDistribution.getKeys()) {
            firstLetterView.getItems().add(new ObservableFrequencyDistribution(false,
                    key, key, firstLetterDistribution.getCount(key),
                    getColumn(),
                    main));
        }


        // Length
        if (lengthDistribution.getNullCount() > 0) {
            lengthView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, lengthDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Integer key : lengthDistribution.getKeys()) {
            lengthView.getItems().add(new ObservableFrequencyDistribution(false,
                    key + "", key, lengthDistribution.getCount(key),
                    getColumn(),
                    main));
        }

        // Prefix
        if (prefixDistribution.getNullCount() > 0) {
            prefixView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, prefixDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (String key : prefixDistribution.getKeys()) {
            prefixView.getItems().add(new ObservableFrequencyDistribution(false,
                    key, key, prefixDistribution.getCount(key),
                    getColumn(),
                    main));
        }

        // Postfix
        if (postfixDistribution.getNullCount() > 0) {
            postfixView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, postfixDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (String key : postfixDistribution.getKeys()) {
            postfixView.getItems().add(new ObservableFrequencyDistribution(false,
                    key, key, postfixDistribution.getCount(key),
                    getColumn(),
                    main));
        }

        // Word count
        if (wordsDistribution.getNullCount() > 0) {
            wordsView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, wordsDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Integer key : wordsDistribution.getKeys()) {
            wordsView.getItems().add(new ObservableFrequencyDistribution(false,
                    key+ "", key, wordsDistribution.getCount(key),
                    getColumn(),
                    main));
        }
    }


}
