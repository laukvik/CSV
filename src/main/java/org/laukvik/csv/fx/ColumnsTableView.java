package org.laukvik.csv.fx;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * @author Morten Laukvik
 */
public class ColumnsTableView extends TableView<ObservableColumn>{

    public ColumnsTableView() {
        super();
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setEditable(true);

        final TableColumn<ObservableColumn, Boolean> checkboxColumn = new TableColumn<>("");
        checkboxColumn.setMinWidth(32);
        checkboxColumn.setMaxWidth(32);
        checkboxColumn.setEditable(true);
        checkboxColumn.setCellValueFactory(new PropertyValueFactory<>("visible"));
        checkboxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxColumn));

        final TableColumn columnNameColumn = new TableColumn("Columns");
        columnNameColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableColumn,String>("name")
        );
        columnNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        getColumns().addAll(checkboxColumn, columnNameColumn);
        columnNameColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ObservableColumn, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<ObservableColumn, String> t) {
                        ((ObservableColumn) t.getTableView().getItems().get( t.getTablePosition().getRow() )).setName(t.getNewValue());
                    }
                }
        );

        /* Resizing */
        setPlaceholder(new Label("No columns available"));
        checkboxColumn.prefWidthProperty().bind(widthProperty().multiply(0.2));
        columnNameColumn.prefWidthProperty().bind(widthProperty().multiply(0.8));
    }
}
