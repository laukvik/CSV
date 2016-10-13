package org.laukvik.csv.fx;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.laukvik.csv.CSV;
import org.laukvik.csv.DistinctColumnValues;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.columns.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * A helper class for the JavaFX application.
 *
 * @author Morten Laukvik
 */
public class Builder {

    public static ResourceBundle getBundle(){
        return ResourceBundle.getBundle("fx");
    }

    public static ImageView getImage(){
        ImageView v =  new ImageView(new Image("feather.png", 48, 200, true, true));
        return v;
    }

    public static java.awt.Dimension getPercentSize(float w, float h){
        java.awt.Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Float width = size.width * w;
        Float height = size.height * h;
        return new java.awt.Dimension( width.intValue(), height.intValue());
    }

    public static ObservableList<ObservableColumn> createVisibleColumnsObservableList(final MetaData md){
        List<ObservableColumn> list = new ArrayList<>();
        for (int x=0; x<md.getColumnCount(); x++){
            Column c = md.getColumn(x);
            if (c.isVisible()){
                list.add( new ObservableColumn(c) );
            }
        }
        return FXCollections.observableArrayList( list );
    }

    public static ObservableList<ObservableColumn> createAllObservableList(final MetaData md){
        List<ObservableColumn> list = new ArrayList<>();
        for (int x=0; x<md.getColumnCount(); x++){
            Column c = md.getColumn(x);
            list.add( new ObservableColumn(c) );
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

    /**
     * Creates the Rows
     *
     * @param resultsTableView
     * @param csv
     */
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
                tc.setCellFactory(TextFieldTableCell.forTableColumn());
                tc.setMinWidth(100);
            }
        }
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
