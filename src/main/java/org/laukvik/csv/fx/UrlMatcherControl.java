package org.laukvik.csv.fx;

import javafx.scene.control.Tab;
import org.laukvik.csv.CSV;
import org.laukvik.csv.statistics.FreqDistribution;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.UrlColumn;
import org.laukvik.csv.query.RowMatcher;
import org.laukvik.csv.query.UrlFileMatcher;
import org.laukvik.csv.query.UrlFilePostfixMatcher;
import org.laukvik.csv.query.UrlFilePrefixMatcher;
import org.laukvik.csv.query.UrlAnchorMatcher;
import org.laukvik.csv.query.UrlHostMatcher;
import org.laukvik.csv.query.UrlMatcher;
import org.laukvik.csv.query.UrlPathMatcher;
import org.laukvik.csv.query.UrlPortMatcher;
import org.laukvik.csv.query.UrlQueryMatcher;
import org.laukvik.csv.query.UrlProtocolMatcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
 */
public class UrlMatcherControl extends ColumnMatcherControl {

    private final Tab uniqueTab;

    private final Tab protocolTab;
    private final Tab hostTab;
    private final Tab portTab;
    private final Tab pathTab;
    private final Tab fileTab;
    private final Tab postfixTab;
    private final Tab prefixTab;
    private final Tab queryTab;
    private final Tab anchorTab;


    private final FrequencyDistributionTableView uniqueView;

    private final FrequencyDistributionTableView protocolView;
    private final FrequencyDistributionTableView hostView;
    private final FrequencyDistributionTableView portView;
    private final FrequencyDistributionTableView pathView;
    private final FrequencyDistributionTableView fileView;
    private final FrequencyDistributionTableView prefixView;
    private final FrequencyDistributionTableView postfixView;
    private final FrequencyDistributionTableView queryView;
    private final FrequencyDistributionTableView anchorView;

    private final Main main;
    private final List<RowMatcher> matchers;

    public UrlMatcherControl(final UrlColumn column, final Main main) {
        super(column);
        this.main = main;
        matchers = new ArrayList<>();
        uniqueView = new FrequencyDistributionTableView();
        uniqueTab = new Tab("Values", uniqueView);
        protocolView = new FrequencyDistributionTableView();
        protocolTab = new Tab("Protocol", protocolView);
        hostView = new FrequencyDistributionTableView();
        hostTab = new Tab("Host");
        hostTab.setContent(hostView);
        portView = new FrequencyDistributionTableView();
        portTab = new Tab("Port", portView);
        pathView = new FrequencyDistributionTableView();
        pathTab = new Tab("Path", pathView);
        fileView = new FrequencyDistributionTableView();
        fileTab = new Tab("File", fileView);
        postfixView = new FrequencyDistributionTableView();
        postfixTab = new Tab("Postfix", postfixView);
        prefixView = new FrequencyDistributionTableView();
        prefixTab = new Tab("Prefix", prefixView);
        queryView = new FrequencyDistributionTableView();
        queryTab = new Tab("Query", queryView);
        anchorView = new FrequencyDistributionTableView();
        anchorTab = new Tab("Anchor", anchorView);
        getTabs().addAll(uniqueTab, hostTab, pathTab, fileTab, prefixTab, postfixTab, queryTab, anchorTab, protocolTab, portTab);
    }

    public List<RowMatcher> getMatchers() {
        UrlColumn sc = (UrlColumn) getColumn();
        matchers.clear();

        List<URL> urlList;
        List<String> stringList;
        List<Integer> intList;

        stringList = findStrings(uniqueView);
        if (!stringList.isEmpty()){
            urlList = new ArrayList<>();
            for (String s : stringList){
                try {
                    urlList.add(new URL(s));
                } catch (MalformedURLException e) {
                }
            }
            matchers.add(new UrlMatcher(sc, urlList));
        }

        stringList = findStrings(protocolView);
        if (!stringList.isEmpty()){
            matchers.add(new UrlProtocolMatcher(sc, stringList));
        }

        stringList = findStrings(hostView);
        if (!stringList.isEmpty()){
            matchers.add(new UrlHostMatcher(sc, stringList));
        }

        stringList = findStrings(pathView);
        if (!stringList.isEmpty()){
            matchers.add(new UrlPathMatcher(sc, stringList));
        }

        stringList = findStrings(fileView);
        if (!stringList.isEmpty()){
            matchers.add(new UrlFileMatcher(sc, stringList));
        }

        stringList = findStrings(prefixView);
        if (!stringList.isEmpty()){
            matchers.add(new UrlFilePrefixMatcher(sc, stringList));
        }
        stringList = findStrings(postfixView);
        if (!stringList.isEmpty()){
            matchers.add(new UrlFilePostfixMatcher(sc, stringList));
        }


        stringList = findStrings(queryView);
        if (!stringList.isEmpty()){
            matchers.add(new UrlQueryMatcher(sc, stringList));
        }

        stringList = findStrings(anchorView);
        if (!stringList.isEmpty()){
            matchers.add(new UrlAnchorMatcher(sc, stringList));
        }

        intList = findIntegers(portView);
        if (!intList.isEmpty()){
            matchers.add(new UrlPortMatcher(sc, intList));
        }

        return matchers;
    }

    public void loadCSV(final CSV csv) {
        UrlColumn uc = (UrlColumn) column;

        FreqDistribution<String> uniqueDistribution = new FreqDistribution<>(column);
        FreqDistribution<String> schemeDistribution = new FreqDistribution<>(column);
        FreqDistribution<String> hostDistribution = new FreqDistribution<>(column);
        FreqDistribution<Integer> portDistribution = new FreqDistribution<>(column);
        FreqDistribution<String> pathDistribution = new FreqDistribution<>(column);
        FreqDistribution<String> fileDistribution = new FreqDistribution<>(column);
        FreqDistribution<String> postfixDistribution = new FreqDistribution<>(column);
        FreqDistribution<String> prefixDistribution = new FreqDistribution<>(column);
        FreqDistribution<String> queryDistribution = new FreqDistribution<>(column);
        FreqDistribution<String> fragmentDistribution = new FreqDistribution<>(column);

        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            URL u = r.getURL(uc);
            if (u == null) {
                uniqueDistribution.addValue(null);
                schemeDistribution.addValue(null);
                hostDistribution.addValue(null);
                portDistribution.addValue(null);
                pathDistribution.addValue(null);
                fileDistribution.addValue(null);
                postfixDistribution.addValue(null);
                prefixDistribution.addValue(null);
                queryDistribution.addValue(null);
                fragmentDistribution.addValue(null);
            } else {
                uniqueDistribution.addValue(u.toExternalForm());
                schemeDistribution.addValue(UrlColumn.getProtocol(u));
                hostDistribution.addValue(u.getHost());
                portDistribution.addValue(UrlColumn.getPort(u));
                pathDistribution.addValue(UrlColumn.getPath(u));
                fileDistribution.addValue(UrlColumn.getFilename(u));
                postfixDistribution.addValue(UrlColumn.getPostfix(u));
                prefixDistribution.addValue(UrlColumn.getPrefix(u));
                queryDistribution.addValue(u.getQuery());
                fragmentDistribution.addValue(UrlColumn.getAnchor(u));
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


        // Scheme
        if (schemeDistribution.getNullCount() > 0) {
            protocolView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, schemeDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (String key : schemeDistribution.getKeys()) {
            protocolView.getItems().add(new ObservableFrequencyDistribution(false,
                    key, key, schemeDistribution.getCount(key),
                    getColumn(),
                    main));
        }

        // Host
        if (hostDistribution.getNullCount() > 0) {
            hostView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, hostDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (String key : hostDistribution.getKeys()) {
            hostView.getItems().add(new ObservableFrequencyDistribution(false,
                    key, key, hostDistribution.getCount(key),
                    getColumn(),
                    main));
        }


        // Path
        if (pathDistribution.getNullCount() > 0) {
            pathView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, pathDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (String key : pathDistribution.getKeys()) {
            pathView.getItems().add(new ObservableFrequencyDistribution(false,
                    key, key, pathDistribution.getCount(key),
                    getColumn(),
                    main));
        }

        // File
        if (fileDistribution.getNullCount() > 0) {
            fileView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, fileDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (String key : fileDistribution.getKeys()) {
            fileView.getItems().add(new ObservableFrequencyDistribution(false,
                    key, key, fileDistribution.getCount(key),
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



        // Port
        if (portDistribution.getNullCount() > 0) {
            portView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, portDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Integer key : portDistribution.getKeys()) {
            portView.getItems().add(new ObservableFrequencyDistribution(false,
                    key + "", key, portDistribution.getCount(key),
                    getColumn(),
                    main));
        }

        // Query
        if (queryDistribution.getNullCount() > 0) {
            queryView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, queryDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (String key : queryDistribution.getKeys()) {
            queryView.getItems().add(new ObservableFrequencyDistribution(false,
                    key, key, queryDistribution.getCount(key),
                    getColumn(),
                    main));
        }
        // Fragment
        if (fragmentDistribution.getNullCount() > 0) {
            anchorView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, fragmentDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (String key : fragmentDistribution.getKeys()) {
            anchorView.getItems().add(new ObservableFrequencyDistribution(false,
                    key, key, fragmentDistribution.getCount(key),
                    getColumn(),
                    main));
        }
    }


}
