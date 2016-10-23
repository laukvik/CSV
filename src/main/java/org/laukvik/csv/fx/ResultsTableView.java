package org.laukvik.csv.fx;

import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.laukvik.csv.CSV;

import java.util.ResourceBundle;

import static org.laukvik.csv.fx.Builder.createResultsColumns;
import static org.laukvik.csv.fx.Builder.createResultsRows;

/**
 * A JavaFX component showing all columns in a CSV.
 *
 * @author Morten Laukvik
 */
class ResultsTableView extends TableView<ObservableRow> {

    public ResultsTableView() {
        super();
        ResourceBundle bundle = Builder.getBundle();
        setEditable(true);
        Label l = new Label(bundle.getString("table.results.empty"));
        setPlaceholder(l);
    }

    public void clearRows(){
        setItems(FXCollections.observableArrayList());
        getColumns().clear();
    }

    public void columnsChanged(CSV csv, Main main) {
        createResultsColumns(this, csv.getMetaData());
        createResultsRows(this, csv, main);
    }
}
