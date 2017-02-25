package org.laukvik.csv.fx;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ResourceBundle;

/**
 * JavaFX table which contains a Frequency distribution table of values and the amount of instance
 * pr item in the list.
 *
 */
public final class FrequencyDistributionTableView extends TableView<ObservableFrequencyDistribution> {

    /**
     * The width of the select column.
     */
    private static final int SELECT_WIDTH = 32;
    /**
     * The width of the count column.
     */
    private static final int COUNT_WIDTH = 96;
    /**
     * The ratio of the select column.
     */
    private static final float SELECT_RATIO = 0.1f;
    /**
     * The ratio of the value column.
     */
    private static final float VALUE_RATIO = 0.7f;
    /**
     * The ratio of the count column.
     */
    private static final float COUNT_RATIO = 0.2f;
    /**
     * The minimum width of the count column.
     */
    private static final int MIN_WIDTH = 32;
    /**
     * The maximum width of the count column.
     */
    private static final int MAX_WIDTH = 120;

    /**
     * Creates a new instance.
     */
    FrequencyDistributionTableView() {
        super();
        ResourceBundle bundle = Builder.getBundle();
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setEditable(true);
        setPlaceholder(new Label(bundle.getString("table.unique.empty")));

        final TableColumn selectUniqueColumn = new TableColumn("");
        selectUniqueColumn.setSortable(false);
        selectUniqueColumn.setMinWidth(SELECT_WIDTH);
        selectUniqueColumn.setMaxWidth(SELECT_WIDTH);
        selectUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableFrequencyDistribution, Boolean>("selected")
        );
        selectUniqueColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectUniqueColumn));
        selectUniqueColumn.setEditable(true);
        getColumns().add(selectUniqueColumn);


        final TableColumn valueUniqueColumn = new TableColumn(bundle.getString("table.unique.values"));
        valueUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableFrequencyDistribution, String>("label")
        );
        getColumns().add(valueUniqueColumn);


        final TableColumn countUniqueColumn = new TableColumn("");
        countUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableFrequencyDistribution, Integer>("count")
        );
        countUniqueColumn.setStyle("-fx-alignment: CENTER_RIGHT");
        countUniqueColumn.setMinWidth(MIN_WIDTH);
        countUniqueColumn.setMaxWidth(MAX_WIDTH);
        countUniqueColumn.setPrefWidth(COUNT_WIDTH);
        getColumns().add(countUniqueColumn);
        selectUniqueColumn.prefWidthProperty().bind(widthProperty().multiply(SELECT_RATIO));
        valueUniqueColumn.prefWidthProperty().bind(widthProperty().multiply(VALUE_RATIO));
        countUniqueColumn.prefWidthProperty().bind(widthProperty().multiply(COUNT_RATIO));
    }
}
