package org.laukvik.csv.fx;

import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.laukvik.csv.CSV;

import static org.laukvik.csv.fx.Builder.createResultsColumns;
import static org.laukvik.csv.fx.Builder.createResultsRows;

/**
 * A JavaFX component showing all columns in a CSV.
 *
 * @author Morten Laukvik
 */
public class ResultsTableView extends TableView<ObservableRow> {

    public ResultsTableView() {
        setEditable(true);
        Label l = new Label("No rows available");
        setPlaceholder(l);
    }

    public void clearRows(){
        setItems(FXCollections.observableArrayList());
        getColumns().clear();
    }

    public void columnsChanged(CSV csv) {
        createResultsColumns(this, csv.getMetaData());
        createResultsRows(this, csv);
    }
}
