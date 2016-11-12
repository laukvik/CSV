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
public class ResultsTableView extends TableView<ObservableRow> {

    /**
     * Creates a new empty instance.
     */
    public ResultsTableView() {
        super();
        ResourceBundle bundle = Builder.getBundle();
        setEditable(true);
        Label l = new Label(bundle.getString("table.results.empty"));
        setPlaceholder(l);
    }

    /**
     * Removes all rows.
     */
    public final void clearRows() {
        setItems(FXCollections.observableArrayList());
        getColumns().clear();
    }

    /**
     * Notifies that the rows where changed and needs rebuilding.
     *
     * @param csv  the csv instance
     * @param main the main instance
     */
    public final void columnsChanged(final CSV csv, final Main main) {
        createResultsColumns(this, csv);
        createResultsRows(this, csv, main);
    }
}
