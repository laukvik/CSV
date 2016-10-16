package org.laukvik.csv.fx;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ResourceBundle;

/**
 * JavaFX table which contains a Frequency distribution table of values and the amount of instance pr item in the list.
 *
 * @author Morten Laukvik
 */
class FrequencyDistributionTableView extends TableView<ObservableFrequencyDistribution> {

    public FrequencyDistributionTableView() {
        super();
        ResourceBundle bundle = Builder.getBundle();
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setEditable(true);
        setPlaceholder(new Label(bundle.getString("table.unique.empty")));

        final TableColumn selectUniqueColumn = new TableColumn("");
        selectUniqueColumn.setSortable(false);
        selectUniqueColumn.setMinWidth(32);
        selectUniqueColumn.setMaxWidth(32);
        selectUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableFrequencyDistribution, Boolean>("selected")
        );
        selectUniqueColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectUniqueColumn));
        selectUniqueColumn.setEditable(true);
        getColumns().add(selectUniqueColumn);
        final TableColumn valueUniqueColumn = new TableColumn(bundle.getString("table.unique.values"));
        valueUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableFrequencyDistribution, String>("value")
        );
        getColumns().add(valueUniqueColumn);
        final TableColumn countUniqueColumn = new TableColumn("");
        countUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableFrequencyDistribution, Integer>("count")
        );
        countUniqueColumn.setStyle("-fx-alignment: CENTER_RIGHT");
        countUniqueColumn.setMinWidth(32);
        countUniqueColumn.setMaxWidth(120);
        countUniqueColumn.setPrefWidth(96);
        getColumns().add(countUniqueColumn);
        selectUniqueColumn.prefWidthProperty().bind(widthProperty().multiply(0.1));
        valueUniqueColumn.prefWidthProperty().bind(widthProperty().multiply(0.7));
        countUniqueColumn.prefWidthProperty().bind(widthProperty().multiply(0.2));
    }
}
