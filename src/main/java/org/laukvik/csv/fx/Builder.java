package org.laukvik.csv.fx;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        return ResourceBundle.getBundle("fx");
    }

    public static ImageView getImage(){
        ImageView v =  new ImageView(new Image("feather.png"));
        return v;
    }

    public static java.awt.Dimension getPercentSize(float w, float h){
        java.awt.Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Float width = size.width * w;
        Float height = size.height * h;
        return new java.awt.Dimension( width.intValue(), height.intValue());
    }

    public static ObservableList<ObservableColumn> createColumnsObservableList(final MetaData md){
        List<ObservableColumn> list = new ArrayList<>();
        for (int x=0; x<md.getColumnCount(); x++){
            Column c = md.getColumn(x);
            if (c.isVisible()){
                list.add( new ObservableColumn(true, c) );
            }
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

    private TableColumn createColumnVisibleColumn(Callback<TableColumn, TableCell> checkBoxFactory) {
        TableColumn activeCol = new TableColumn("Active");
        activeCol.setMinWidth(25);
        activeCol.setCellValueFactory(new PropertyValueFactory<ObservableColumn, Boolean>("active"));
        activeCol.setCellFactory(checkBoxFactory);
        activeCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ObservableColumn, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ObservableColumn, Boolean> isVisible) {

            }
        });
        return activeCol;
    }

    public static TableView<ObservableColumn> buildColumnsTable(){
        TableView<ObservableColumn> columnsTableView = new TableView<ObservableColumn>();
        columnsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        columnsTableView.setEditable(true);


        /*       Checkbox          */
        final TableColumn<ObservableColumn, Boolean> checkboxColumn = new TableColumn<>("");
        checkboxColumn.setMinWidth(32);
        checkboxColumn.setMaxWidth(32);
        checkboxColumn.setEditable(true);
        checkboxColumn.setCellValueFactory(new PropertyValueFactory<ObservableColumn,Boolean>("visible"));
        checkboxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxColumn));
        checkboxColumn.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<ObservableColumn, Boolean>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<ObservableColumn, Boolean> t) {
                    System.out.println( "visible: " + t.getNewValue() );
                    ((ObservableColumn) t.getTableView().getItems().get( t.getTablePosition().getRow() )).setVisible(t.getNewValue());
                }
            }
        );

//        TableColumn<Job,Boolean>  checkCol = new TableColumn<>("Check");
//        checkCol.setCellValueFactory( new PropertyValueFactory<Job,Boolean>( "checkBoxValue" ) );
//        checkCol.setCellFactory( CheckBoxTableCell.forTableColumn( checkCol ) );

        checkboxColumn.setEditable(true);
//        checkboxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxColumn));
//        checkboxColumn.setCellValueFactory(new PropertyValueFactory<ObservableColumn,Boolean>("selected"));
//        checkboxColumn.setEditable(true);  // Checkboxen werden editierbar gemacht


        /*          Name          */
        final TableColumn columnNameColumn = new TableColumn("Name");
        columnNameColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableColumn,String>("name")
        );
        columnNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        columnsTableView.getColumns().addAll(checkboxColumn, columnNameColumn);
        columnNameColumn.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<ObservableColumn, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<ObservableColumn, String> t) {
                    ((ObservableColumn) t.getTableView().getItems().get( t.getTablePosition().getRow() )).setName(t.getNewValue());
                }
            }
        );

        /* Resizing */
        columnsTableView.setPlaceholder(new Label("No columns"));
        checkboxColumn.prefWidthProperty().bind(columnsTableView.widthProperty().multiply(0.2));
        columnNameColumn.prefWidthProperty().bind(columnsTableView.widthProperty().multiply(0.8));
        return columnsTableView;
    }

    public static TableView<ObservableUnique> buildUniqueTable(){
        TableView<ObservableUnique> uniqueTableView = new TableView<>();
        uniqueTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        uniqueTableView.setEditable(true);

        final TableColumn selectUniqueColumn = new TableColumn("");
        selectUniqueColumn.setSortable(false);
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
        final TableColumn countUniqueColumn = new TableColumn("");
        countUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<ObservableUnique,Integer>("count")
        );
        countUniqueColumn.setStyle("-fx-alignment: CENTER_RIGHT");
        countUniqueColumn.setMinWidth(32);
        countUniqueColumn.setMaxWidth(120);
        countUniqueColumn.setPrefWidth(96);
        uniqueTableView.getColumns().add(countUniqueColumn);
        selectUniqueColumn.prefWidthProperty().bind(uniqueTableView.widthProperty().multiply(0.1));
        valueUniqueColumn.prefWidthProperty().bind(uniqueTableView.widthProperty().multiply(0.7));
        countUniqueColumn.prefWidthProperty().bind(uniqueTableView.widthProperty().multiply(0.2));
        return uniqueTableView;
    }

    public static void createResultsRows(final TableView<ObservableRow> resultsTableView, final CSV csv){
        resultsTableView.getItems().clear();
        for (int y=0; y<csv.getRowCount(); y++){
            resultsTableView.getItems().add(new ObservableRow(csv.getRow(y)));
        }
    }

    public static void createResultsColumns(final TableView<ObservableRow> resultsTableView, final MetaData md){
        resultsTableView.getColumns().clear();
        for (int x = 0; x < md.getColumnCount(); x++) {
            final Column c = md.getColumn(x);
            if (c.isVisible()){
                final TableColumn<ObservableRow, String> tc = new TableColumn<>(c.getName());
                tc.setCellFactory(TextFieldTableCell.forTableColumn());
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
        }
    }

    public static MenuBar buildMenuBar(final Main main){
        final MenuBar menu = new MenuBar();
        menu.setUseSystemMenuBar(true);
        // ----- File -----
        final Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(KeyCombination.keyCombination("Meta+n"));
        newItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.newFile();
            }
        });

        MenuItem openItem = new MenuItem("Open");
        openItem.setAccelerator(KeyCombination.keyCombination("Meta+o"));
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.openFileDialog();
            }
        });


        MenuItem openFileOptions = new MenuItem("Open...");
        openFileOptions.setAccelerator(KeyCombination.keyCombination("Meta+o+shift"));
        openFileOptions.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.openFileDialogWithOptions();
            }
        });

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setAccelerator(KeyCombination.keyCombination("Meta+s"));
        MenuItem saveAsItem = new MenuItem("Save as");
        saveAsItem.setAccelerator(KeyCombination.keyCombination("Meta+s+shift"));
        MenuItem exportItem = new MenuItem("Export");
        exportItem.setAccelerator(KeyCombination.keyCombination("Meta+e"));

        MenuItem printItem = new MenuItem("Print");
        printItem.setAccelerator(KeyCombination.keyCombination("Meta+p"));
        printItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handlePrintAction();
            }
        });
        fileMenu.getItems().addAll(newItem, openItem, openFileOptions, saveItem, saveAsItem, exportItem, printItem);



        // ----- Edit ------
        final Menu edit = new Menu("Edit");
        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setAccelerator(KeyCombination.keyCombination("Meta+x"));
        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setAccelerator(KeyCombination.keyCombination("Meta+c"));
        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setAccelerator(KeyCombination.keyCombination("Meta+v"));
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setAccelerator(KeyCombination.keyCombination("Meta+backspace"));
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.out.println(t);
                main.handleDeleteAction();
            }
        });
        edit.getItems().addAll(cutItem, copyItem, pasteItem, deleteItem);

        // ----- Insert ------
        final Menu insert = new Menu("Sett inn");
        MenuItem newColumnItem = new MenuItem("Kolonne");
        newColumnItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleNewColumnAction();
            }
        });
        MenuItem newRowItem = new MenuItem("Rad");
        newRowItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.out.println(t);
                main.handleNewRowAction();
            }
        });
        MenuItem headersRowItem = new MenuItem("Headers");
        headersRowItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.out.println(t);
                main.handleNewHeaders();
            }
        });
        insert.getItems().addAll(newColumnItem,newRowItem,headersRowItem);

        // ----- Help ------
        final Menu help = new Menu("Help");

        //
        menu.getMenus().add(fileMenu);
        menu.getMenus().add(edit);
        menu.getMenus().add(insert);
        menu.getMenus().add(help);

        return menu;
    }

    public static String toKb(long value){
        if (value < 1000) {
            return (value) + " bytes";
        } else {
            return (value / 1024) + " KB";
        }
    }

    public static String getSeparatorString(char separator){
        switch(separator){
            case CSV.COMMA : return "COMMA";
            case CSV.TAB : return "TAB";
            case CSV.PIPE : return "PIPE";
            case CSV.SEMICOLON : return "SEMICOLON";
            default : return "COMMA";
        }
    }

    public static Character getSeparatorCharByString(String separator){
        switch(separator){
            case "COMMA" : return CSV.COMMA;
            case "TAB" : return CSV.TAB;
            case "PIPE" : return CSV.PIPE;
            case "SEMICOLON" : return CSV.SEMICOLON;
            default : return null;
        }
    }

}
