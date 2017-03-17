package org.laukvik.csv.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.laukvik.csv.CSV;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder that produces Charts.
 */
public final class ChartBuilder {

    /**
     * The maximum number of items in pie chart.
     */
    private static final int PIE_CHART_MAX = 50;

    /**
     * Hide default constructor.
     */
    private ChartBuilder() {
    }

    public static PieChart buildPieChart(final CSV csv) {
        List<PieChart.Data> dataset = new ArrayList<>();
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(dataset);
        return new PieChart(data);
    }

    public static PieChart buildPieChart(final ColumnMatcherControl columnMatcherControl) {
        int tabIndex = columnMatcherControl.getSelectionModel().getSelectedIndex();
        FrequencyDistributionTableView uniqueView = columnMatcherControl.getFrequencyDistributionTableView(tabIndex);
        List<PieChart.Data> dataset = new ArrayList<>();
        int max = uniqueView.getItems().size();

        int selectCount = 0;

        for (int y= 0; y< max; y++){
            ObservableFrequencyDistribution ofd = uniqueView.getItems().get(y);
            selectCount += ofd.isSelected() ? 1 : 0;
        }

        for (int y= 0; y< max; y++){
            ObservableFrequencyDistribution ofd = uniqueView.getItems().get(y);
            if (selectCount > 0 && ofd.isSelected() || selectCount == 0){
                if (y < PIE_CHART_MAX){
                    dataset.add(new PieChart.Data(ofd.labelProperty().getValue(), ofd.countProperty().intValue()));
                }
            }
        }


        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(dataset);
        PieChart chart = new PieChart(data);
        chart.setLegendVisible(false);
        return chart;
    }

}
