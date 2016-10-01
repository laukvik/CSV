package org.laukvik.csv.fx;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCombination;
import javafx.util.Callback;
import org.laukvik.csv.CSV;
import org.laukvik.csv.DistinctColumnValues;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.columns.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 *
 * @author Morten Laukvik
 */
public class Builder {

    public static ResourceBundle getBundle(){
        return ResourceBundle.getBundle("messages");
    }

    public static java.awt.Dimension getPercentSize(float w, float h){
        java.awt.Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Float width = size.width * w;
        Float height = size.height * h;
        return new java.awt.Dimension( width.intValue(), height.intValue());
    }

    public static ObservableList<ObservableColumn> createColumnsObservableList(final CSV csv){
        List<ObservableColumn> list = new ArrayList<>();
        MetaData md = csv.getMetaData();
        for (int x=0; x<md.getColumnCount(); x++){
            list.add( new ObservableColumn(true, md.getColumn(x)) );
        }
        return FXCollections.observableArrayList( list );
    }

    public static ObservableList<ObservableUnique> createUniqueObservableList(int columnIndex, CSV csv){
        List<ObservableUnique> list = new ArrayList<>();
        DistinctColumnValues d = csv.getDistinctColumnValues(columnIndex);
        for (String key :d.getKeys()){
            list.add(new ObservableUnique(false,key,d.getCount(key)));
        }
        return FXCollections.observableArrayList( list );
    }

    public static TableView<ObservableColumn> buildColumnsTable(){
        TableView<ObservableColumn> columnsTableView = new TableView<>();
        columnsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        columnsTableView.setEditable(true);
        /* Checkbox */
        final TableColumn checkboxColumn = new TableColumn("Visible");
        checkboxColumn.setMinWidth(32);
        checkboxColumn.setMaxWidth(32);
        checkboxColumn.setCellValueFactory(
                new PropertyValueFactory<Column,Boolean>("selected")
        );
        checkboxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxColumn));
        /* Name */
        final TableColumn columnNameColumn = new TableColumn("Name");
        columnsTableView.getColumns().add(checkboxColumn);
        columnNameColumn.setCellValueFactory(
                new PropertyValueFactory<Column,String>("name")
        );
        columnNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        columnsTableView.getColumns().add(columnNameColumn);
        checkboxColumn.prefWidthProperty().bind(columnsTableView.widthProperty().multiply(0.2));
        columnNameColumn.prefWidthProperty().bind(columnsTableView.widthProperty().multiply(0.8));
        return columnsTableView;
    }

    public static TableView<ObservableUnique> buildUniqueTable(){
        TableView<ObservableUnique> uniqueTableView = new TableView<>();
        uniqueTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        uniqueTableView.setEditable(true);
        final TableColumn selectUniqueColumn = new TableColumn("Selected");
        selectUniqueColumn.setMinWidth(32);
        selectUniqueColumn.setMaxWidth(32);
        selectUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableUnique,Boolean>("selected")
        );
        selectUniqueColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectUniqueColumn));
        selectUniqueColumn.setEditable(true);
        uniqueTableView.getColumns().add(selectUniqueColumn);
        final TableColumn valueUniqueColumn = new TableColumn("Value");
        valueUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableUnique,String>("value")
        );
        uniqueTableView.getColumns().add(valueUniqueColumn);
        final TableColumn countUniqueColumn = new TableColumn("Count");
        countUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableUnique,Integer>("count")
        );
        countUniqueColumn.setMinWidth(32);
        countUniqueColumn.setMaxWidth(64);
        uniqueTableView.getColumns().add(countUniqueColumn);
        selectUniqueColumn.prefWidthProperty().bind(uniqueTableView.widthProperty().multiply(0.1));
        valueUniqueColumn.prefWidthProperty().bind(uniqueTableView.widthProperty().multiply(0.7));
        countUniqueColumn.prefWidthProperty().bind(uniqueTableView.widthProperty().multiply(0.2));
        return uniqueTableView;
    }

    public static void fillData(final TableView<ObservableRow> resultsTableView, final CSV csv){
        resultsTableView.getItems().clear();
        resultsTableView.getColumns().clear();
        MetaData md = csv.getMetaData();
        for (int x = 0; x < md.getColumnCount(); x++) {
            Column c = md.getColumn(x);
            TableColumn<ObservableRow, String> tc = new TableColumn<>(c.getName());
            resultsTableView.getColumns().add(tc);
            final int colX = x;
            tc.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<ObservableRow, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableRow, String> param) {
                            return param.getValue().getValue(colX);
                        }
                    }
            );
            tc.setCellFactory(TextFieldTableCell.<ObservableRow>forTableColumn());
            tc.setMinWidth(100);
        }
        for (int y=0; y<csv.getRowCount(); y++){
            resultsTableView.getItems().add(new ObservableRow(csv.getRow(y)));
        }

    }

    public static MenuBar buildMenuBar(){
        final MenuBar menu = new MenuBar();
        menu.setUseSystemMenuBar(true);
        // ----- File -----
        final Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(KeyCombination.keyCombination("Meta+n"));
        newItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                newCSV();
            }
        });

        // ----- Edit ------
        final Menu edit = new Menu("Edit");

        // ----- Options ------
        final Menu options = new Menu("Options");


        // ----- Help ------
        final Menu help = new Menu("Help");

        //
        menu.getMenus().add(fileMenu);
        menu.getMenus().add(edit);
        menu.getMenus().add(options);
        menu.getMenus().add(help);

        return menu;
    }

}
