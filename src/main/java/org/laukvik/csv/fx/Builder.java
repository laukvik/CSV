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
import org.laukvik.csv.statistics.FrequencyDistribution;
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
final class Builder {

    /**
     * The width of the logo in pixels.
     */
    private static final int LOGO_WIDTH = 48;
    /**
     * The height of the logo in pixels.
     */
    private static final int LOGO_HEIGHT = 48;
    /**
     * The default width of columns in the result sets.
     */
    private static final int COLUMN_WIDTH_DEFAULT = 100;
    /**
     * The value of one kilobyte.
     */
    private static final int KILOBYTE = 1024;

    /**
     * Hides the constructor.
     */
    private Builder() {
    }

    /**
     * Returns the ResourceBundle for the application.
     *
     * @return the ResourceBundle
     */
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("messages");
    }


    /**
     * Returns the Image symobolizeing the application.
     *
     * @return the Image
     */
    public static ImageView getImage() {
        return new ImageView(new Image("feather.png", LOGO_WIDTH, LOGO_HEIGHT, true, true));
    }

    /**
     * Returns the dimension of the screen by specifying the percentage in float values.
     * <br>
     * <pre>
     * Example: 1 = 100% and 0.5f = 50%.
     * </pre>
     *
     * @param w the width in percent
     * @param h the height in percent
     * @return the Dimension
     */
    public static java.awt.Dimension getPercentSize(final float w, final float h) {
        java.awt.Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Float width = size.width * w;
        Float height = size.height * h;
        return new java.awt.Dimension(width.intValue(), height.intValue());
    }

    /**
     * Builds an ObservableList of all Columns found in the MetaData.
     *
     * @param csv the csv
     * @return ObservableList of all Columns
     */
    public static ObservableList<ObservableColumn> createAllObservableList(final CSV csv) {
        List<ObservableColumn> list = new ArrayList<>();
        for (int x = 0; x < csv.getColumnCount(); x++) {
            Column c = csv.getColumn(x);
            list.add(new ObservableColumn(c));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * Builds an ObservableList of FrequencyDistribution.
     *
     * @param columnIndex the columnIndex
     * @param csv         the CSV
     * @param main        the Main
     * @return ObservableList of FrequencyDistribution
     */
    public static ObservableList<ObservableFrequencyDistribution> createFrequencyDistributionObservableList(
            final int columnIndex, final CSV csv, final Main main) {
        List<ObservableFrequencyDistribution> list = new ArrayList<>();
        FrequencyDistribution d = csv.buildFrequencyDistribution(columnIndex);
        Column c = csv.getColumn(columnIndex);
        for (String key : d.getKeys()) {
            boolean selected = main.getQueryModel().isSelected(c, key);
            list.add(new ObservableFrequencyDistribution(selected, key, key, d.getCount(key), c, main));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * Creates the Rows.
     *
     * @param resultsTableView the TableView
     * @param csv              the csv file
     * @param main             the Main
     */
    public static void createResultsRows(final TableView<ObservableRow> resultsTableView,
                                         final CSV csv,
                                         final Main main) {
        resultsTableView.getItems().clear();
        Query query = main.getQuery();
        if (query != null) {
            List<Row> rows = csv.getRowsByQuery(query);
            for (int y = 0; y < rows.size(); y++) {
                resultsTableView.getItems().add(new ObservableRow(rows.get(y), csv, main));
            }
        } else {
            for (int y = 0; y < csv.getRowCount(); y++) {
                resultsTableView.getItems().add(new ObservableRow(csv.getRow(y), csv, main));
            }
        }
    }

    /**
     * Removes all columns in the resultsTableView and populates it with all columns found in the MetaData.
     *
     * @param resultsTableView the resultsTableView
     * @param csv the csv
     */
    public static void createResultsColumns(final TableView<ObservableRow> resultsTableView, final CSV csv) {
        resultsTableView.getColumns().clear();
        for (int x = 0; x < csv.getColumnCount(); x++) {
            final Column c = csv.getColumn(x);
            if (c.isVisible()) {
                final TableColumn<ObservableRow, String> tc = new TableColumn<>(c.getName());
                tc.setCellFactory(TextFieldTableCell.forTableColumn());
                resultsTableView.getColumns().add(tc);
                final int colX = x;
                tc.setCellValueFactory(
                        new Callback<TableColumn.CellDataFeatures<ObservableRow, String>, ObservableValue<String>>() {
                            @Override
                            public ObservableValue<String> call(
                                    final TableColumn.CellDataFeatures<ObservableRow, String> param) {
                                return param.getValue().getValue(colX);
                            }
                        }
                );
                tc.setCellFactory(TextFieldTableCell.forTableColumn());
                tc.setMinWidth(COLUMN_WIDTH_DEFAULT);
            }
        }
    }


    /**
     * Returns the value formatted in Kilobytes.
     *
     * @param value the value
     * @return the formatted value in Kilobytes
     */
    public static String toKb(final long value) {
        if (value < KILOBYTE) {
            return (value) + " bytes";
        } else {
            return (value / KILOBYTE) + " KB";
        }
    }

    /**
     * Returns the String representation of the separator character.
     *
     * @param separator the separator
     * @return the String representation
     */
    public static String getSeparatorString(final char separator) {
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

    /**
     * Returns the char representation of the separator character.
     *
     * @param separator the separator
     * @return the String representation
     */
    public static Character getSeparatorCharByString(final String separator) {
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

    /**
     * Returns true if operating system is Mac.
     *
     * @return true if Mac
     */
    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }
}
