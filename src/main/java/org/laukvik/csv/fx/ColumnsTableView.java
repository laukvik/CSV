package org.laukvik.csv.fx;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.ResourceBundle;

/**
 * JavaFX table component with the available columns in CSV.
 *
 * @author Morten Laukvik
 */
class ColumnsTableView extends TableView<ObservableColumn> {

    /**
     * The width of the checkbox column.
     */
    private static final int CHECKBOX_WIDTH = 32;

    /**
     *  The width ratio of checkbox.
     */
    private static final float CHECKBOX_RATIO = 0.2f;
    /**
     * The width ratio of column.
     */
    private static final float COLUMN_RATIO = 1 - CHECKBOX_RATIO;

    /**
     * A TableView representing a Column in the CSV.
     */
    ColumnsTableView() {
        super();
        ResourceBundle bundle = Builder.getBundle();

        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setEditable(true);

        final TableColumn<ObservableColumn, Boolean> checkboxColumn = new TableColumn<>("");
        checkboxColumn.setMinWidth(CHECKBOX_WIDTH);
        checkboxColumn.setMaxWidth(CHECKBOX_WIDTH);
        checkboxColumn.setEditable(true);
        checkboxColumn.setCellValueFactory(new PropertyValueFactory<>("visible"));
        checkboxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxColumn));

        final TableColumn columnNameColumn = new TableColumn(bundle.getString("table.columns"));
        columnNameColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableColumn, String>("name")
        );
        columnNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        getColumns().addAll(checkboxColumn, columnNameColumn);
        columnNameColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ObservableColumn, String>>() {
                    @Override
                    public void handle(final TableColumn.CellEditEvent<ObservableColumn, String> t) {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue());
                }
                }
        );

        /* Resizing */
        setPlaceholder(new Label(bundle.getString("table.columns.empty")));
        checkboxColumn.prefWidthProperty().bind(widthProperty().multiply(CHECKBOX_RATIO));
        columnNameColumn.prefWidthProperty().bind(widthProperty().multiply(COLUMN_RATIO));
    }

}
