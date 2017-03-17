package org.laukvik.csv.fx;

import javafx.scene.control.Tab;
import org.laukvik.csv.CSV;
import org.laukvik.csv.statistics.FreqDistribution;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.query.DateIsMatcher;
import org.laukvik.csv.query.DateOfMonthMatcher;
import org.laukvik.csv.query.HourMatcher;
import org.laukvik.csv.query.MillisecondMatcher;
import org.laukvik.csv.query.MinuteMatcher;
import org.laukvik.csv.query.MonthMatcher;
import org.laukvik.csv.query.RowMatcher;
import org.laukvik.csv.query.SecondMatcher;
import org.laukvik.csv.query.WeekMatcher;
import org.laukvik.csv.query.WeekdayMatcher;
import org.laukvik.csv.query.YearMatcher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * JavaFX control which enables selection of date details.
 * <p>
 * Range: from-to
 * Unique
 * Year
 * Month
 * Date
 * Week
 * Weekday
 */
public class DateMatcherControl extends ColumnMatcherControl {


    private final FrequencyDistributionTableView uniqueView;
    private final FrequencyDistributionTableView yearView;
    private final FrequencyDistributionTableView monthView;
    private final FrequencyDistributionTableView dateView;
    private final FrequencyDistributionTableView weekView;
    private final FrequencyDistributionTableView weekdayView;

    private final FrequencyDistributionTableView hourView;
    private final FrequencyDistributionTableView minuteView;
    private final FrequencyDistributionTableView secondView;
    private final FrequencyDistributionTableView millisecondView;

    private Main main;

    public DateMatcherControl(final DateColumn column, final Main main) {
        super(column);
        this.main = main;
        uniqueView = new FrequencyDistributionTableView();
        Tab uniqueTab = new Tab("Values", uniqueView);
        yearView = new FrequencyDistributionTableView();
        Tab yearTab = new Tab("Year", yearView);
        monthView = new FrequencyDistributionTableView();
        Tab monthTab = new Tab("Month", monthView);
        dateView = new FrequencyDistributionTableView();
        Tab dateTab = new Tab("Date", dateView);
        weekView = new FrequencyDistributionTableView();
        Tab weekTab = new Tab("Week", weekView);
        weekdayView = new FrequencyDistributionTableView();
        Tab weekdayTab = new Tab("Weekday", weekdayView);
        hourView = new FrequencyDistributionTableView();
        Tab hourTab = new Tab("Hour", hourView);
        minuteView = new FrequencyDistributionTableView();
        Tab minuteTab = new Tab("Hour", minuteView);
        secondView = new FrequencyDistributionTableView();
        Tab secondTab = new Tab("Second", secondView);
        millisecondView = new FrequencyDistributionTableView();
        Tab millisecondTab = new Tab("Millis", millisecondView);
        getTabs().addAll(uniqueTab, yearTab, monthTab, dateTab, weekTab, weekdayTab,
                hourTab, minuteTab, secondTab, millisecondTab);
    }

    public void loadCSV(final CSV csv) {
        uniqueView.getItems().clear();
        yearView.getItems().clear();
        monthView.getItems().clear();
        dateView.getItems().clear();
        weekView.getItems().clear();
        weekdayView.getItems().clear();

        hourView.getItems().clear();
        minuteView.getItems().clear();
        secondView.getItems().clear();
        millisecondView.getItems().clear();

        FreqDistribution<Date> uniqueDistribution = new FreqDistribution<>(column);
        FreqDistribution<Integer> yearDistribution = new FreqDistribution<>(column);
        FreqDistribution<Integer> monthDistribution = new FreqDistribution<>(column);
        FreqDistribution<Integer> dateDistribution = new FreqDistribution<>(column);
        FreqDistribution<Integer> weekDistribution = new FreqDistribution<>(column);
        FreqDistribution<Integer> weekdayDistribution = new FreqDistribution<>(column);
        FreqDistribution<Integer> hourDistribution = new FreqDistribution<>(column);
        FreqDistribution<Integer> minuteDistribution = new FreqDistribution<>(column);
        FreqDistribution<Integer> secondDistribution = new FreqDistribution<>(column);
        FreqDistribution<Integer> millisecondDistribution = new FreqDistribution<>(column);

        DateColumn dc = (DateColumn) column;
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            Date d = r.getDate(dc);
            uniqueDistribution.addValue(d);
            if (d == null) {
                yearDistribution.addValue(null);
                monthDistribution.addValue(null);
                dateDistribution.addValue(null);
                weekDistribution.addValue(null);
                weekdayDistribution.addValue(null);
                hourDistribution.addValue(null);
                minuteDistribution.addValue(null);
                secondDistribution.addValue(null);
                millisecondDistribution.addValue(null);
            } else {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(d);
                //
                yearDistribution.addValue(cal.get(Calendar.YEAR));
                monthDistribution.addValue(cal.get(Calendar.MONTH));
                dateDistribution.addValue(cal.get(Calendar.DATE));
                weekDistribution.addValue(cal.get(Calendar.WEEK_OF_YEAR));
                weekdayDistribution.addValue(cal.get(Calendar.DAY_OF_WEEK));

                hourDistribution.addValue(cal.get(Calendar.HOUR_OF_DAY));
                minuteDistribution.addValue(cal.get(Calendar.MINUTE));
                secondDistribution.addValue(cal.get(Calendar.SECOND));
                millisecondDistribution.addValue(cal.get(Calendar.MILLISECOND));
            }
        }
        // Date
        if (uniqueDistribution.getNullCount() > 0) {
            uniqueView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, uniqueDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Date key : uniqueDistribution.getKeys()) {
            uniqueView.getItems().add(new ObservableFrequencyDistribution(false,
                    DateColumn.formatDefaultDate(key), key, uniqueDistribution.getCount(key),
                    getColumn(),
                    main));
        }
        // Year
        if (yearDistribution.getNullCount() > 0) {
            yearView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, yearDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Integer key : yearDistribution.getKeys()) {
            yearView.getItems().add(new ObservableFrequencyDistribution(false,
                    key + "", key, yearDistribution.getCount(key),
                    getColumn(),
                    main));
        }
        // Month
        if (monthDistribution.getNullCount() > 0) {
            monthView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, monthDistribution.getNullCount(),
                    getColumn(),
                    main));
        }

        for (Integer key : monthDistribution.getKeys()) {
            monthView.getItems().add(new ObservableFrequencyDistribution(false,
                    getLanguage("date.month." + key), key, monthDistribution.getCount(key),
                    getColumn(),
                    main));
        }
        // Date
        if (dateDistribution.getNullCount() > 0) {
            dateView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, dateDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Integer key : dateDistribution.getKeys()) {
            dateView.getItems().add(new ObservableFrequencyDistribution(false,
                    key + "", key, dateDistribution.getCount(key),
                    getColumn(),
                    main));
        }
        // Week
        if (weekDistribution.getNullCount() > 0) {
            weekView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, weekDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Integer key : weekDistribution.getKeys()) {
            weekView.getItems().add(new ObservableFrequencyDistribution(false,
                    key + "", key, weekDistribution.getCount(key),
                    getColumn(),
                    main));
        }
        // Weekday
        if (weekdayDistribution.getNullCount() > 0) {
            weekdayView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, weekdayDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Integer key : weekdayDistribution.getKeys()) {
            weekdayView.getItems().add(new ObservableFrequencyDistribution(false,
                    getLanguage("date.weekday." + key), key, weekdayDistribution.getCount(key),
                    getColumn(),
                    main));
        }


        // Hour
        if (hourDistribution.getNullCount() > 0) {
            hourView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, hourDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Integer key : hourDistribution.getKeys()) {
            hourView.getItems().add(new ObservableFrequencyDistribution(false,
                    key+ "", key, hourDistribution.getCount(key),
                    getColumn(),
                    main));
        }
        // Minute
        if (minuteDistribution.getNullCount() > 0) {
            minuteView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, minuteDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Integer key : minuteDistribution.getKeys()) {
            minuteView.getItems().add(new ObservableFrequencyDistribution(false,
                    key+ "", key, minuteDistribution.getCount(key),
                    getColumn(),
                    main));
        }
        // Second
        if (secondDistribution.getNullCount() > 0) {
            secondView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, secondDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Integer key : secondDistribution.getKeys()) {
            secondView.getItems().add(new ObservableFrequencyDistribution(false,
                    key+ "", key, secondDistribution.getCount(key),
                    getColumn(),
                    main));
        }
        // Millis
        if (millisecondDistribution.getNullCount() > 0) {
            millisecondView.getItems().add(new ObservableFrequencyDistribution(false,
                    EMPTY, null, millisecondDistribution.getNullCount(),
                    getColumn(),
                    main));
        }
        for (Integer key : millisecondDistribution.getKeys()) {
            millisecondView.getItems().add(new ObservableFrequencyDistribution(false,
                    key+ "", key, millisecondDistribution.getCount(key),
                    getColumn(),
                    main));
        }

    }



    public List<RowMatcher> getMatchers() {
        DateColumn dc = (DateColumn) getColumn();
        List<RowMatcher> matchers = new ArrayList<>();

        List<Integer> intList;
        List<String> stringList;
        List<Date> dateList;

        dateList = findDates(uniqueView);
        if (!dateList.isEmpty()) {
            matchers.add(new DateIsMatcher(dc, dateList));
        }

        intList = findIntegers(yearView);
        if (!intList.isEmpty()) {
            matchers.add(new YearMatcher(dc, intList));
        }

        intList = findIntegers(monthView);
        if (!intList.isEmpty()) {
            matchers.add(new MonthMatcher(dc, intList));
        }

        intList = findIntegers(dateView);
        if (!intList.isEmpty()) {
            matchers.add(new DateOfMonthMatcher(dc, intList));
        }

        intList = findIntegers(weekView);
        if (!intList.isEmpty()) {
            matchers.add(new WeekMatcher(dc, intList));
        }

        intList = findIntegers(weekdayView);
        if (!intList.isEmpty()) {
            matchers.add(new WeekdayMatcher(dc, intList));
        }



        intList = findIntegers(hourView);
        if (!intList.isEmpty()) {
            matchers.add(new HourMatcher(dc, intList));
        }
        intList = findIntegers(minuteView);
        if (!intList.isEmpty()) {
            matchers.add(new MinuteMatcher(dc, intList));
        }
        intList = findIntegers(secondView);
        if (!intList.isEmpty()) {
            matchers.add(new SecondMatcher(dc, intList));
        }
        intList = findIntegers(millisecondView);
        if (!intList.isEmpty()) {
            matchers.add(new MillisecondMatcher(dc, intList));
        }


        return matchers;
    }


}
