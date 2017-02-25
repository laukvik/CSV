package org.laukvik.csv.fx;

import javafx.geometry.Side;
import javafx.scene.control.TabPane;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.query.RowMatcher;
import org.laukvik.csv.statistics.BigDecimalRange;
import org.laukvik.csv.statistics.DoubleRange;
import org.laukvik.csv.statistics.FloatRange;
import org.laukvik.csv.statistics.IntegerRange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 *
 *
 *
 */
public abstract class ColumnMatcherControl extends TabPane {

    final String EMPTY = Builder.getBundle().getString("not.available");

    final Column column;

    public ColumnMatcherControl(final Column column){
        super();
        setSide(Side.BOTTOM);
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        this.column = column;
    }

    public String getLanguage(String key){
        return Builder.getBundle().getString(key);
    }

    public abstract List<RowMatcher> getMatchers();

    public abstract void loadCSV(CSV csv);

    public Column getColumn() {
        return column;
    }

    public List<Float> findFloats(final FrequencyDistributionTableView view){
        List<Float> list = new ArrayList<>();
        for (ObservableFrequencyDistribution omd : view.getItems()){
            if (omd.isSelected()){
                Float i =  (Float) omd.valueProperty().getValue();
                list.add(i);
            }
        }
        return list;
    }

    public List<Double> findDoubles(final FrequencyDistributionTableView view){
        List<Double> list = new ArrayList<>();
        for (ObservableFrequencyDistribution omd : view.getItems()){
            if (omd.isSelected()){
                Double i =  (Double) omd.valueProperty().getValue();
                list.add(i);
            }
        }
        return list;
    }

    public List<BigDecimal> findBigDecimals(final FrequencyDistributionTableView view){
        List<BigDecimal> list = new ArrayList<>();
        for (ObservableFrequencyDistribution omd : view.getItems()){
            if (omd.isSelected()){
                BigDecimal i =  (BigDecimal) omd.valueProperty().getValue();
                list.add(i);
            }
        }
        return list;
    }

    public List<Integer> findIntegers(final FrequencyDistributionTableView view){
        List<Integer> list = new ArrayList<>();
        for (ObservableFrequencyDistribution omd : view.getItems()){
            if (omd.isSelected()){
                Integer i =  (Integer) omd.valueProperty().getValue();
                list.add(i);
            }
        }
        return list;
    }

    public List<String> findStrings(final FrequencyDistributionTableView view){
        List<String> list = new ArrayList<>();
        for (ObservableFrequencyDistribution omd : view.getItems()){
            if (omd.isSelected()){
                String i =  (String) omd.valueProperty().getValue();
                list.add(i);
            }
        }
        return list;
    }

    public List<Boolean> findBooleans(final FrequencyDistributionTableView view){
        List<Boolean> list = new ArrayList<>();
        for (ObservableFrequencyDistribution omd : view.getItems()){
            if (omd.isSelected()){
                String v = (String) omd.valueProperty().getValue();
                if (v.equalsIgnoreCase("odd")){
                    list.add(Boolean.FALSE);
                } else  if (v.equalsIgnoreCase("even")){
                    list.add(Boolean.TRUE);
                }

            }
        }
        return list;
    }

    public List<Date> findDates(final FrequencyDistributionTableView view){
        List<Date> list = new ArrayList<>();
        for (ObservableFrequencyDistribution omd : view.getItems()){
            if (omd.isSelected()){
                Date i =  (Date) omd.valueProperty().getValue();
                list.add(i);
            }
        }
        return list;
    }

    public List<IntegerRange> findIntegerRanges(final FrequencyDistributionTableView view){
        List<IntegerRange> list = new ArrayList<>();
        for (ObservableFrequencyDistribution omd : view.getItems()){
            if (omd.isSelected()){
                IntegerRange i =  (IntegerRange) omd.valueProperty().getValue();
                list.add(i);
            }
        }
        return list;
    }

    public List<FloatRange> findFloatRanges(final FrequencyDistributionTableView view){
        List<FloatRange> list = new ArrayList<>();
        for (ObservableFrequencyDistribution omd : view.getItems()){
            if (omd.isSelected()){
                FloatRange i =  (FloatRange) omd.valueProperty().getValue();
                list.add(i);
            }
        }
        return list;
    }

    public List<DoubleRange> findDoubleRanges(final FrequencyDistributionTableView view){
        List<DoubleRange> list = new ArrayList<>();
        for (ObservableFrequencyDistribution omd : view.getItems()){
            if (omd.isSelected()){
                DoubleRange i =  (DoubleRange) omd.valueProperty().getValue();
                list.add(i);
            }
        }
        return list;
    }

    public List<BigDecimalRange> findBigDecimalRanges(final FrequencyDistributionTableView view){
        List<BigDecimalRange> list = new ArrayList<>();
        for (ObservableFrequencyDistribution omd : view.getItems()){
            if (omd.isSelected()){
                BigDecimalRange i =  (BigDecimalRange) omd.valueProperty().getValue();
                list.add(i);
            }
        }
        return list;
    }

}
