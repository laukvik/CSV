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
import org.laukvik.csv.FrequencyDistribution;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * A helper class for the JavaFX application.
 *
 * @author Morten Laukvik
 */
class Builder {

    /**
     * Hides the constructor.
     */
    private Builder() {
    }

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("messages");
    }

    public static ImageView getImage() {
        return new ImageView(new Image("feather.png", 48, 200, true, true));
    }

    public static java.awt.Dimension getPercentSize(final float w, final float h) {
        java.awt.Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Float width = size.width * w;
        Float height = size.height * h;
        return new java.awt.Dimension(width.intValue(), height.intValue());
    }

    public static ObservableList<ObservableColumn> createAllObservableList(final MetaData md) {
        List<ObservableColumn> list = new ArrayList<>();
        for (int x = 0; x < md.getColumnCount(); x++) {
            Column c = md.getColumn(x);
            list.add(new ObservableColumn(c));
        }
        return FXCollections.observableArrayList(list);
    }

    public static ObservableList<ObservableFrequencyDistribution> createFrequencyDistributionObservableList(int columnIndex, CSV csv, Main main) {
        List<ObservableFrequencyDistribution> list = new ArrayList<>();
        FrequencyDistribution d = csv.buildFrequencyDistribution(columnIndex);
        Column c = csv.getMetaData().getColumn(columnIndex);
        for (String key : d.getKeys()) {
            boolean selected = main.getQueryModel().isSelected(c, key);
            list.add(new ObservableFrequencyDistribution(selected, key, d.getCount(key), c, main));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * Creates the Rows
     *
     * @param resultsTableView the TableView
     * @param csv              the csv file
     * @param main             the Main
     */
    public static void createResultsRows(final TableView<ObservableRow> resultsTableView, final CSV csv, final Main main) {
        resultsTableView.getItems().clear();
        if (csv.hasQuery()) {
            Query query = csv.getQuery();
            List<Row> rows = query.where().getResultList();
            for (int y = 0; y < rows.size(); y++) {
                resultsTableView.getItems().add(new ObservableRow(rows.get(y), main));
            }
        } else {
            for (int y = 0; y < csv.getRowCount(); y++) {
                resultsTableView.getItems().add(new ObservableRow(csv.getRow(y), main));
            }
        }
    }

    public static void createResultsColumns(final TableView<ObservableRow> resultsTableView, final MetaData md) {
        resultsTableView.getColumns().clear();
        for (int x = 0; x < md.getColumnCount(); x++) {
            final Column c = md.getColumn(x);
            if (c.isVisible()) {
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

    public static String toKb(long value) {
        if (value < 1000) {
            return (value) + " bytes";
        } else {
            return (value / 1024) + " KB";
        }
    }

    public static String getSeparatorString(char separator) {
        switch (separator) {
            case CSV.COMMA:
                return "COMMA";
            case CSV.TAB:
                return "TAB";
            case CSV.PIPE:
                return "PIPE";
            case CSV.SEMICOLON:
                return "SEMICOLON";
            default:
                return "COMMA";
        }
    }

    public static Character getSeparatorCharByString(String separator) {
        switch (separator) {
            case "COMMA":
                return CSV.COMMA;
            case "TAB":
                return CSV.TAB;
            case "PIPE":
                return CSV.PIPE;
            case "SEMICOLON":
                return CSV.SEMICOLON;
            default:
                return null;
        }
    }

    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }
}
