package org.laukvik.csv.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.laukvik.csv.CSV;
import org.laukvik.csv.ChangeListener;
import org.laukvik.csv.FileListener;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.io.BOM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;
import static org.laukvik.csv.fx.Builder.createAllObservableList;
import static org.laukvik.csv.fx.Builder.createFrequencyDistributionObservableList;
import static org.laukvik.csv.fx.Builder.createResultsColumns;
import static org.laukvik.csv.fx.Builder.createResultsRows;
import static org.laukvik.csv.fx.Builder.getPercentSize;
import static org.laukvik.csv.fx.Builder.getSeparatorCharByString;
import static org.laukvik.csv.fx.Builder.getSeparatorString;
import static org.laukvik.csv.fx.Builder.toKb;

//import static org.laukvik.csv.fx.Builder.getSeparatorCharByString;

/**
 * The JavaFX desktop application for opening and displaying the data sets.
 *
 * @author Morten Laukvik
 */
public class Main extends Application implements ChangeListener, FileListener {

    /**
     * The ResourceBundle for this application.
     */
    private final ResourceBundle bundle = Builder.getBundle();
    /**
     * The CSV model.
     */
    private CSV csv;
    /**
     * The JavaFX stage.
     */
    private Stage stage;
    /**
     * The TableView for column selection.
     */
    private ColumnsTableView columnsTableView;
    /**
     * The TableView for FrequencyDistribution.
     */
    private FrequencyDistributionTableView frequencyDistributionTableView;
    /**
     * The TableView for results columns.
     */
    private ResultsTableView resultsTableView;
    /**
     * The Label for showing the amount of rows.
     */
    private Label rowsLabel;
    /**
     * The Label for showing the amount of columns.
     */
    private Label colsLabel;
    /**
     * The Label for showing the encoding.
     */
    private Label encodingLabel;
    /**
     * The Label for showing the file size.
     */
    private Label sizeLabel;
    /**
     * The Label for showing the separator.
     */
    private Label separatorLabel;
    /**
     * The Label for showing the progress bar.
     */
    private ProgressBar progressBar;
    /**
     * The Recent manager class.
     */
    private Recent recent;
    /**
     * The MenuBar and all it.
     */
    private CsvMenuBar menuBar;
    /**
     * The ScrollPane to display results and other components in.
     */
    private ScrollPane resultsScroll;
    /**
     * The view mode.
     */
    private ViewMode viewMode = ViewMode.Results;
    /**
     * The query model.
     */
    private QueryModel queryModel;

    /**
     * Can be run from commandline.
     *
     * @param args arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }

    /**
     * Copies the selected row to Clipboard.
     * <p>
     * TODO - This should be in native CSV format - not TAB!
     *
     * @param rowIndex the row index
     * @param csv      the csv
     * @return the string representation of the row
     */
    private static String toClipboardString(final int rowIndex, final CSV csv) {
        StringBuilder b = new StringBuilder();
        for (int x = 0; x < csv.getMetaData().getColumnCount(); x++) {
            if (x > 0) {
                b.append(CSV.TAB);
            }
            StringColumn sc = (StringColumn) csv.getMetaData().getColumn(x);
            b.append(csv.getRow(rowIndex).getString(sc));
        }
        return b.toString();
    }

    /**
     * Builds a pie chart.
     * <p>
     * TODO - Extract this to separate class and extrac max as a final int
     *
     * @param frequencyDistributionTableView the frequencyDistributionTableView
     * @return piechart
     */
    public static PieChart buildPieChart(final FrequencyDistributionTableView frequencyDistributionTableView) {
        List<PieChart.Data> dataset = new ArrayList<>();
        int max = 50;
        int x = 0;
        for (ObservableFrequencyDistribution fd : frequencyDistributionTableView.getItems()) {
            if (fd.isSelected()) {
                dataset.add(new PieChart.Data(fd.getValue(), fd.getCount()));
            }
        }
        if (dataset.isEmpty()) {
            for (ObservableFrequencyDistribution fd : frequencyDistributionTableView.getItems()) {
                if (x < max) {
                    dataset.add(new PieChart.Data(fd.getValue(), fd.getCount()));
                }
                x++;
            }
        }
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(dataset);
        return new PieChart(data);
    }

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the stage
     */
    public final void start(final Stage primaryStage) {
        this.stage = primaryStage;
        columnsTableView = new ColumnsTableView();
        frequencyDistributionTableView = new FrequencyDistributionTableView();
        frequencyDistributionTableView.getSelectionModel().getSelectedIndices().addListener(
                new ListChangeListener<Integer>() {
                    @Override
                    public void onChanged(final Change<? extends Integer> c) {
                        int rowIndex = frequencyDistributionTableView.getSelectionModel().getSelectedIndex();
                        if (viewMode == ViewMode.Preview) {
                            handleViewPreviewAction();
                        } else if (viewMode == ViewMode.Wikipedia) {
                            handleViewWikipediaAction();
                        } else if (viewMode == ViewMode.Maps) {
                            handleViewGoogleMapsAction();
                        } else if (viewMode == ViewMode.Search) {
                            handleViewGoogleSearchAction();
                        }
                    }
                });
        resultsTableView = new ResultsTableView();

        final ScrollPane columnsScroll = new ScrollPane(columnsTableView);
        columnsScroll.setFitToHeight(true);
        columnsScroll.setFitToWidth(true);

        final ScrollPane uniqueScroll = new ScrollPane(frequencyDistributionTableView);
        uniqueScroll.setFitToHeight(true);
        uniqueScroll.setFitToWidth(true);

        columnsTableView.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
            @Override
            public void onChanged(final Change<? extends Integer> c) {
                int rowIndex = columnsTableView.getSelectionModel().getSelectedIndex();
                setSelectedColumnIndex(rowIndex);
            }
        });

        resultsScroll = new ScrollPane(resultsTableView);
        resultsScroll.setFitToHeight(true);
        resultsScroll.setFitToWidth(true);

        final SplitPane tableSplit = new SplitPane(columnsScroll, uniqueScroll);
        tableSplit.setOrientation(Orientation.VERTICAL);
        tableSplit.setDividerPosition(0, 0.25);
        final SplitPane mainSplit = new SplitPane(tableSplit, resultsScroll);
        mainSplit.setDividerPositions(0.2);

        final VBox topContainer = new VBox();

        menuBar = new CsvMenuBar(this);
        topContainer.getChildren().add(menuBar);


        final ToolBar bar = new ToolBar();
        Label rows = new Label(bundle.getString("status.rows"));

        rows.setDisable(true);
        rowsLabel = new Label("-");
        Label cols = new Label(bundle.getString("status.columns"));
        cols.setDisable(true);
        colsLabel = new Label("-");
        Label encoding = new Label(bundle.getString("status.encoding"));
        encoding.setDisable(true);
        encodingLabel = new Label("-");
        Label size = new Label(bundle.getString("status.size"));
        size.setDisable(true);
        sizeLabel = new Label("-");
        Label separator = new Label(bundle.getString("status.separator"));
        separator.setDisable(true);
        separatorLabel = new Label("-");
        progressBar = new ProgressBar(100);
        progressBar.setVisible(false);
        progressBar.setPrefWidth(200);
        bar.getItems().addAll(rows, rowsLabel, cols, colsLabel, size, sizeLabel, encoding, encodingLabel,
                separator, separatorLabel, progressBar);

        final BorderPane root = new BorderPane();
        root.setTop(topContainer);
        root.setCenter(mainSplit);
        root.setBottom(bar);

        final java.awt.Dimension percent = getPercentSize(0.8f, 0.7f);
        final Scene scene = new Scene(root, percent.getWidth(), percent.getHeight());
        stage.setScene(scene);
        stage.show();
        recent = new Recent(Recent.getConfigurationFile(), 10);
        menuBar.buildRecentList(recent);
        newFile();
    }

    /**
     * Returns the QueryModel being used.
     *
     * @return the QueryModel
     */
    public final QueryModel getQueryModel() {
        return queryModel;
    }

    /**
     * Sets the selected column by it index.
     *
     * @param selectedColumnIndex the selectedColumnIndex
     */
    private void setSelectedColumnIndex(final int selectedColumnIndex) {
        if (selectedColumnIndex > -1) {
            frequencyDistributionTableView.setItems(
                    createFrequencyDistributionObservableList(
                            selectedColumnIndex,
                            csv,
                            this));
            if (viewMode == ViewMode.Results) {
                handleViewResultsAction();
            } else if (viewMode == ViewMode.Chart) {
                handleViewChartAction();
            } else if (viewMode == ViewMode.Preview) {
                handleViewPreviewAction();
            }
        }
    }

    /**
     * Handles opening files.
     */
    public final void handleFileOpen() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.open"));
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(FileChooserExtensions.buildCSV());
        final File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            loadFile(selectedFile);
        }
    }

    /**
     * Loads a file without dialogs.
     *
     * @param file the file to open
     */
    public final void loadFile(final File file) {
        if (file != null) {
            if (file.getName().endsWith(".properties")) {
                loadResourceBundle(file);
            } else if (file.getName().endsWith(".txt")) {
                loadWordCountFile(file);
            } else {
                loadFile(file, null, null);
            }
        }
    }

    /**
     * Handles opening file with options.
     */
    public final void handleOpenFileWithOptions() {
        final Dialog dialog = new Dialog();
        dialog.setTitle(bundle.getString("app.title"));
        dialog.setHeaderText(bundle.getString("dialog.file.open"));

        final GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(10, 20, 10, 20));
        gridpane.setHgap(20);
        gridpane.setVgap(10);

        final Label separatorLabel = new Label(bundle.getString("metadata.separator"));
        gridpane.add(separatorLabel, 0, 1);
        final ChoiceBox separatorBox = new ChoiceBox();
        List<String> items = new ArrayList<>();
        items.add(bundle.getString("metadata.separator.autodetect"));
        for (char c : CSV.listSupportedSeparatorChars()) {
            items.add(Builder.getSeparatorString(c));
        }
        separatorBox.getItems().addAll(items);
        gridpane.add(separatorBox, 1, 1);
        separatorBox.getSelectionModel().select(0);

        final Label charsetLabel = new Label(bundle.getString("metadata.encoding"));
        gridpane.add(charsetLabel, 0, 2);
        final ChoiceBox charsetBox = new ChoiceBox();
        charsetBox.getItems().add(bundle.getString("metadata.encoding.autodetect"));
        for (BOM b : BOM.values()) {
            charsetBox.getItems().add(b.name());
        }
        for (String key : Charset.availableCharsets().keySet()) {
            charsetBox.getItems().add(key);
        }
        charsetBox.getSelectionModel().select(0);
        gridpane.add(charsetBox, 1, 2);
        dialog.getDialogPane().setContent(gridpane);

        // Set the button types.
        ButtonType okButtonType = ButtonType.OK;
        ButtonType cancelButtonType = ButtonType.CANCEL;
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);
        dialog.showAndWait();
        ButtonType resultButtonType = (ButtonType) dialog.getResult();

        if (resultButtonType.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
            Character separator;
            if (separatorBox.getSelectionModel().getSelectedIndex() == 0) {
                separator = null;
            } else {
                separator = getSeparatorCharByString((String) separatorBox.getSelectionModel().getSelectedItem());
            }
            Charset charset;
            if (charsetBox.getSelectionModel().getSelectedIndex() == 0) {
                charset = null;
            } else {
                charset = Charset.forName((String) charsetBox.getSelectionModel().getSelectedItem());
            }
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(bundle.getString("dialog.file.open"));
            fileChooser.setSelectedExtensionFilter(FileChooserExtensions.buildCSV());
            final File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                loadFile(selectedFile, separator, charset);
            }
        }
    }

    /**
     * Clears all existing data in the model.
     */
    public final void newFile() {
        csv = new CSV();
        queryModel = new QueryModel(csv, this);
        csv.addChangeListener(this);
        csv.addFileListener(this);
        columnsTableView.setItems(observableArrayList());
        frequencyDistributionTableView.setItems(observableArrayList());
        resultsTableView.clearRows();
        updateToolbar();
    }

    /**
     * Loads a CSV file using the specific separator and charset.
     *
     * @param file          the file to load
     * @param separatorChar the separator char to use
     * @param charset       the charset to use
     */
    public final void loadFile(final File file, final Character separatorChar, final Charset charset) {
        newFile();
        try {
            if (charset == null && separatorChar == null) {
                csv.readFile(file);
            } else if (charset != null) {
                csv.readFile(file, charset, separatorChar);
            } else if (separatorChar != null) {
                csv.readFile(file, separatorChar);
            }

            queryModel = new QueryModel(csv, this);

            if (csv.getFile() != null) {
                recent.open(file);
                menuBar.buildRecentList(recent);
            }
        } catch (IOException e) {
            alert(e.getMessage());
        }
    }

    /**
     * Load a ResourceBundle from file.
     *
     * @param file the file
     */
    public final void loadResourceBundle(final File file) {
        newFile();
        try {
            csv.readResourceBundle(file);
            if (csv.getFile() != null) {
                recent.open(file);
                menuBar.buildRecentList(recent);
            }
        } catch (FileNotFoundException e) {
            alert(e.getMessage());
        }
    }

    /**
     * Opens a file for counting its words.
     *
     * @param file the file
     */
    public final void loadWordCountFile(final File file) {
        newFile();
        try {
            csv.readWordCountFile(file);
            if (csv.getFile() != null) {
                recent.open(file);
                menuBar.buildRecentList(recent);
            }
        } catch (FileNotFoundException e) {
            alert(e.getMessage());
        }
    }

    /**
     * Updates the toolbar with details.
     */
    private void updateToolbar() {
        rowsLabel.setText(csv.getRowCount() + "");
        colsLabel.setText(csv.getMetaData().getColumnCount() + "");
        if (csv.getMetaData().getCharset() == null) {
            encodingLabel.setText(bundle.getString("metadata.encoding.na"));
        } else {
            encodingLabel.setText(csv.getMetaData().getCharset().name());
        }
        sizeLabel.setText(toKb(csv.getFile() == null ? 0 : csv.getFile().length()) + "");
        if (csv.getMetaData().getSeparatorChar() == null) {
            separatorLabel.setText("");
        } else {
            separatorLabel.setText(getSeparatorString(csv.getMetaData().getSeparatorChar()));
        }
        stage.setTitle(csv.getFile() == null ? "" : csv.getFile().getName() + "");
    }

    /**
     * Shows a dialog box with the error message.
     * @param message the error message
     */
    private void alert(final String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("app.title"));
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * Implemented by ChangeListener.
     *
     * @param column the column
     * @see ChangeListener
     */
    public final void columnCreated(final Column column) {
        buildResultsTable();
        updateToolbar();
    }

    /**
     * Implemented by ChangeListener.
     *
     * @param column the column
     * @see ChangeListener
     */
    public final void columnUpdated(final Column column) {
        buildResultsTable();
        updateToolbar();
    }

    /**
     * Implemented by ChangeListener.
     *
     * @param columnIndex the column
     * @see ChangeListener
     */
    public final void columnRemoved(final int columnIndex) {
        buildResultsTable();
        updateToolbar();
    }

    /**
     * Implemented by ChangeListener.
     *
     * @param fromIndex the index to move from
     * @param toIndex the destination
     * @see ChangeListener
     */
    public final void columnMoved(final int fromIndex, final int toIndex) {
        buildResultsTable();
    }

    /**
     * Implemented by ChangeListener.
     *
     * @param rowIndex the index which was removed
     * @param row the row
     * @see ChangeListener
     */
    public final void rowRemoved(final int rowIndex, final Row row) {
        resultsTableView.getItems().remove(rowIndex);
        updateToolbar();
    }

    /**
     * Implemented by ChangeListener.
     *
     * @param rowIndex the index which was removed
     * @param row the row
     * @see ChangeListener
     */
    public final void rowCreated(final int rowIndex, final Row row) {
        if (rowIndex == resultsTableView.getItems().size() + 1) {
            resultsTableView.getItems().add(new ObservableRow(row, this));
        } else {
            resultsTableView.getItems().add(rowIndex, new ObservableRow(row, this));
        }
        updateToolbar();
    }

    /**
     * Implemented by ChangeListener.
     *
     * @param fromRowIndex the old index
     * @param toRowIndex the new index
     * @see ChangeListener
     */
    public final void rowMoved(final int fromRowIndex, final int toRowIndex) {
        Collections.swap(resultsTableView.getItems(), fromRowIndex, toRowIndex);
    }

    /**
     * Implemented by ChangeListener.
     *
     * @param fromRowIndex the from index
     * @param toRowIndex the to index
     * @see ChangeListener
     */
    public final void rowsRemoved(final int fromRowIndex, final int toRowIndex) {
        resultsTableView.getItems().remove(fromRowIndex, toRowIndex);
    }

    /**
     * Implemented by ChangeListener.
     *
     * @param metaData the metaData
     * @see ChangeListener
     */
    public final void metaDataRead(final MetaData metaData) {
        columnsTableView.setItems(createAllObservableList(metaData));
        createResultsColumns(resultsTableView, metaData);
    }

    /**
     * Implemented by ChangeListener.
     *
     * @param columnIndex the column
     * @param rowIndex the row
     * @see ChangeListener
     */
    public final void cellUpdated(final int columnIndex, final int rowIndex) {
        if (columnIndex == columnsTableView.getSelectionModel().getSelectedIndex()) {
            setSelectedColumnIndex(columnIndex);
        }
    }

    /**
     * Builds FrequencyDistribution data.
     *
     */
    public final void buildFrequencyDistribution() {
        int selectedColumnIndex = frequencyDistributionTableView.getSelectionModel().getSelectedIndex();
        if (selectedColumnIndex > -1) {
            frequencyDistributionTableView.getItems().clear();
            frequencyDistributionTableView.setItems(
                    createFrequencyDistributionObservableList(selectedColumnIndex, csv, this));
        }
    }

    /**
     * Starts reading the file.
     *
     * @param file the file
     */
    public final void beginRead(final File file) {
        progressBar.setVisible(true);
    }

    /**
     * Finished reading the file.
     *
     * @param file the file
     */
    public final void finishRead(final File file) {
        progressBar.setVisible(false);
        stage.setTitle(file.getAbsolutePath());
        setSelectedColumnIndex(0);
    }

    /**
     * Read more bytes of the file.
     *
     * @param count the amount of bytes read
     * @param total the total of bytes in the file
     */
    public final void readBytes(final long count, final long total) {
        Platform.runLater(() -> progressBar.setProgress(count / (total * 1f)));
    }

    /**
     * Starts writing the file.
     *
     * @param file the file
     */
    public final void beginWrite(final File file) {
        alert("beginWrite: " + file.getName());
    }

    /**
     * Finished reading the file.
     *
     * @param file the file
     */
    public final void finishWrite(final File file) {
        alert("finishWrite: " + file.getName());
    }

    /**
     * Handles the delete action.
     */
    public final void handleDeleteAction() {
        Node owner = stage.getScene().getFocusOwner();
        if (owner == resultsTableView) {
            handleDeleteRow(resultsTableView.getSelectionModel().getSelectedIndex());
        } else if (owner == columnsTableView) {
            handleDeleteColumn(columnsTableView.getSelectionModel().getSelectedIndex());
        } else if (owner == frequencyDistributionTableView) {
            handleDeleteUnique(frequencyDistributionTableView.getSelectionModel().getSelectedIndex());
        }
    }

    /**
     * Handles the deletion of a unqiue value.
     *
     * todo - Should it be possible to delete rows with this values
     *
     * @param columnIndex the index of the column to delete
     */
    private void handleDeleteUnique(final int columnIndex) {
        ObservableFrequencyDistribution u = frequencyDistributionTableView.getItems().get(columnIndex);
    }

    /**
     * Handles the deletion of a column.
     *
     * @param columnIndex the index of the column
     */
    private void handleDeleteColumn(final int columnIndex) {
        MessageFormat format = new MessageFormat(bundle.getString("dialog.deletecolumn.confirm"));
        Object[] messageArguments = {
                csv.getMetaData().getColumn(columnIndex).getName()
        };
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("app.title"));
        alert.setHeaderText(bundle.getString("dialog.deletecolumn"));
        alert.setContentText(format.format(messageArguments));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            deleteColumn(columnIndex);
        }
    }

    /**
     * Builds the results table.
     *
     */
    private void buildResultsTable() {
        resultsTableView.columnsChanged(csv, this);
    }

    /**
     * Updates the columns.
     */
    private void updateColumns() {
        columnsTableView.setItems(createAllObservableList(csv.getMetaData()));
        createResultsColumns(resultsTableView, csv.getMetaData());
    }

    /**
     * Updates the rows.
     */
    private void updateRows() {
        createResultsColumns(resultsTableView, csv.getMetaData());
        createResultsRows(resultsTableView, csv, this);
    }

    /**
     * Deletes a column.
     *
     * @param columnIndex the index of the column
     */
    private void deleteColumn(final int columnIndex) {
        csv.removeColumn(csv.getMetaData().getColumn(columnIndex));
        updateColumns();
        updateRows();
        int columnCount = csv.getMetaData().getColumnCount();
        if (columnCount == 0) {
            // Nothing to select
        } else if (columnIndex > columnCount - 1) {
            // Deleted last
            columnsTableView.getSelectionModel().select(columnCount - 1);
            columnsTableView.getFocusModel().focus(columnCount - 1);
        } else if (columnIndex < columnCount) {
            columnsTableView.getSelectionModel().select(columnIndex);
            columnsTableView.getFocusModel().focus(columnIndex);
        }
    }

    /**
     * Handles the deletion of a row.
     *
     * @param rowIndex the row to delete
     */
    private void handleDeleteRow(final int rowIndex) {
        csv.removeRow(rowIndex);
        resultsTableView.getSelectionModel().select(rowIndex);
    }

    /**
     * Handles printing action.
     *
     */
    public final void handlePrintAction() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(bundle.getString("app.title"));
        dialog.setHeaderText(bundle.getString("dialog.print"));
        dialog.setGraphic(Builder.getImage());

        final ChoiceBox printerChoiceBox = new ChoiceBox();
        for (Printer p : Printer.getAllPrinters()) {
            printerChoiceBox.getItems().addAll(p);
        }

        if (Printer.getAllPrinters().isEmpty()) {
            alert(bundle.getString("dialog.print.printers.empty"));
        } else {
            dialog.getDialogPane().setContent(printerChoiceBox);
            dialog.showAndWait();

            final Printer printer = (Printer) printerChoiceBox.getSelectionModel().getSelectedItem();
            if (printer != null) {
                PrinterJob printerJob = PrinterJob.createPrinterJob(printer);
                if (printerJob.showPrintDialog(stage)) {
                    printerJob.printPage(resultsTableView);
                    alert(bundle.getString("dialog.print.finished"));
                }
            }
        }
    }

    /**
     * Handles new column action.
     *
     */
    public final void handleNewColumnAction() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(bundle.getString("app.title"));
        dialog.setHeaderText(bundle.getString("dialog.newcolumn"));
        dialog.setContentText(bundle.getString("dialog.newcolumn.columnname"));
        dialog.setGraphic(Builder.getImage());
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            csv.addColumn(new StringColumn(result.get()));
        }
    }

    /**
     * Handles new row action.
     *
     */
    public final void handleNewRowAction() {
        int rowIndex = resultsTableView.getSelectionModel().getSelectedIndex();
        if (rowIndex == -1) {
            csv.addRow();
        } else {
            csv.addRow(rowIndex);
        }
    }

    /**
     * Handles insert new headers action.
     *
     */
    public final void handleNewHeaders() {
        csv.insertHeaders();
        updateRows();
    }

    /**
     * Handles copy action.
     */
    public final void handleCopyAction() {
        int rowIndex = resultsTableView.getSelectionModel().getSelectedIndex();
        if (rowIndex > -1) {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(toClipboardString(rowIndex, csv));
            clipboard.setContent(content);
        }
    }

    /**
     * Handles paste action.
     *
     */
    public final void handlePasteAction() {
        int rowIndex = resultsTableView.getSelectionModel().getSelectedIndex();
        if (rowIndex > -1) {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            String pasted = (String) clipboard.getContent(DataFormat.PLAIN_TEXT);
            String[] values = pasted.split(CSV.TAB + "");
            Row r = csv.addRow(rowIndex);
            for (int x = 0; x < values.length; x++) {
                String value = values[x];
                Column c = csv.getMetaData().getColumn(x);
                r.updateColumn(c, value);
            }
            updateRows();
            resultsTableView.getSelectionModel().select(rowIndex);
        }
    }

    /**
     * Handles cut action.
     */
    public final void handleCutAction() {
        int rowIndex = resultsTableView.getSelectionModel().getSelectedIndex();
        if (rowIndex > -1) {
            handleCopyAction();
            csv.removeRow(rowIndex);
            resultsTableView.getItems().remove(rowIndex);
            resultsTableView.getSelectionModel().select(rowIndex);
        }
    }

    /**
     * Handels move up action.
     */
    public final void handleMoveUpAction() {
        Node owner = stage.getScene().getFocusOwner();
        if (owner == resultsTableView) {
            int rowIndex = resultsTableView.getSelectionModel().getSelectedIndex();
            if (rowIndex > 0) {
                moveRow(rowIndex, rowIndex - 1);
            }
        } else if (owner == columnsTableView) {
            int rowIndex = columnsTableView.getSelectionModel().getSelectedIndex();
            if (rowIndex > 0) {
                moveColumn(rowIndex, rowIndex - 1);
            }
        }
    }

    /**
     * Handles move down action.
     */
    public final void handleMoveDownAction() {
        Node owner = stage.getScene().getFocusOwner();
        if (owner == resultsTableView) {
            int rowIndex = resultsTableView.getSelectionModel().getSelectedIndex();
            if (rowIndex < csv.getRowCount() - 1) {
                moveRow(rowIndex, rowIndex + 1);
            }
        } else if (owner == columnsTableView) {
            int rowIndex = columnsTableView.getSelectionModel().getSelectedIndex();
            if (rowIndex < columnsTableView.getItems().size() - 1) {
                moveColumn(rowIndex, rowIndex + 1);
            }
        }
    }

    /**
     * Moves the row to a new index.
     *
     * @param index the row to move
     * @param toIndex the new index
     */
    private void moveRow(final int index, final int toIndex) {
        csv.moveRow(index, toIndex);
        resultsTableView.getSelectionModel().select(toIndex);
    }

    /**
     * Changes the sort order of a column.
     *
     * @param fromIndex the old index
     * @param toIndex the new index
     */
    private void moveColumn(final int fromIndex, final int toIndex) {
        csv.getMetaData().moveColumn(fromIndex, toIndex);
        Collections.swap(columnsTableView.getItems(), fromIndex, toIndex);
    }

    /**
     * Handles export to JSON action.
     */
    public final void handleExportJsonAction() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.export.json"));
        fileChooser.getExtensionFilters().addAll(FileChooserExtensions.buildJsonFile());
        final File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try {
                csv.writeJSON(selectedFile);
            } catch (Exception e) {
                alert(bundle.getString("file.export.json.failed"));
            }
        }
    }

    /**
     * Handles export to XML action.
     */
    public final void handleExportXmlAction() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.export.xml"));
        fileChooser.getExtensionFilters().addAll(FileChooserExtensions.buildXmlFile());
        final File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try {
                csv.writeXML(selectedFile);
            } catch (Exception e) {
                alert(bundle.getString("file.export.xml.failed"));
            }
        }
    }

    /**
     * Handles export to HTML action.
     */
    public final void handleExportHtmlAction() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.export.html"));
        fileChooser.getExtensionFilters().addAll(FileChooserExtensions.buildHtmlFile());
        final File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try {
                csv.writeHtml(selectedFile);
            } catch (Exception e) {
                alert(bundle.getString("file.export.html.failed"));
            }
        }
    }

    /**
     * Handles opening ResourceBundle action.
     */
    public final void handleOpenResourceBundleAction() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.export.resourcebundle"));
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(FileChooserExtensions.buildResourceBundle());
        final File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try {
                csv.writeResourceBundle(selectedFile);
            } catch (Exception e) {
                alert(bundle.getString("file.export.resourcebundle.failed"));
            }
        }
    }

    /**
     * Handles about action.
     */
    public final void handleAboutAction() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(Builder.getImage());
        alert.setTitle(bundle.getString("app.title"));
        alert.setHeaderText(bundle.getString("app.title") + " v1.0");
        alert.setContentText(bundle.getString("help.about.description"));
        alert.showAndWait();
    }

    /**
     * Handles save as action.
     *
     */
    public final void handleSaveAsAction() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.saveas"));
        final File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try {
                csv.writeFile(selectedFile);
            } catch (Exception e) {
                alert(bundle.getString("dialog.file.saveas.failed"));
            }
        }
    }

    /**
     * Adds a new selection with the value in that column.
     *
     * @param column the column
     * @param value the value of the column
     */
    public final void handleSelected(final Column column, final String value) {
        getQueryModel().addSelection(column, value);
        List<ObservableRow> list = getQueryModel().buildObservableRows();
        resultsTableView.getItems().clear();
        resultsTableView.getItems().addAll(list);
        if (viewMode == ViewMode.Chart) {
            handleViewChartAction();
        }
    }

    /**
     * Removes the selection with the value in that column.
     *
     * @param column the column
     * @param value the value of the column
     */
    public final void handleUnselected(final Column column, final String value) {
        getQueryModel().removeSelection(column, value);
        List<ObservableRow> list = getQueryModel().buildObservableRows();
        resultsTableView.getItems().clear();
        resultsTableView.getItems().addAll(list);
        if (viewMode == ViewMode.Chart) {
            handleViewChartAction();
        }
    }

    /**
     * Handles new query action.
     */
    public final void handleNewQuery() {
        getQueryModel().clearSelections();
        List<ObservableRow> list = getQueryModel().buildObservableRows();
        resultsTableView.getItems().clear();
        resultsTableView.getItems().addAll(list);
        buildFrequencyDistribution();
    }

    /**
     * Handles chart action.
     */
    public final void handleViewChartAction() {
        final PieChart chart = buildPieChart(frequencyDistributionTableView);
        resultsScroll.setContent(chart);
        viewMode = ViewMode.Chart;
    }

    /**
     * Handles view results action.
     */
    public final void handleViewResultsAction() {
        resultsScroll.setContent(resultsTableView);
        viewMode = ViewMode.Results;
    }

    /**
     * Handles preview action.
     */
    public final void handleViewPreviewAction() {
        ObservableFrequencyDistribution ofd = frequencyDistributionTableView.getSelectionModel().getSelectedItem();
        if (ofd != null && ofd.getValue() != null && !ofd.getValue().isEmpty()) {
            String filename = ofd.getValue();
            if (filename == null || filename.trim().isEmpty()) {
                resultsScroll.setContent(new Label(bundle.getString("view.preview.empty")));
            } else if (filename.startsWith("http")) {
                WebView v = new WebView();
                WebEngine webEngine = v.getEngine();
                resultsScroll.setContent(v);
                webEngine.load(filename);

            } else if (filename.endsWith(".gif") || filename.endsWith(".jpg") || filename.endsWith(".png")) {
                if (filename.indexOf('\\') > -1) {
                    filename = filename.replace('\\', '/');
                }
                Path p = Paths.get(csv.getFile().getParent(), filename);
                File f = p.toFile();
                if (f.exists()) {
                    resultsScroll.setContent(new ImageView(new Image(f.toURI().toString())));
                }
            } else {
                resultsScroll.setContent(new Label(bundle.getString("view.preview.empty")));
            }
        }
        viewMode = ViewMode.Preview;
    }

    /**
     * Handles Wikipedia action.
     */
    public final void handleViewWikipediaAction() {
        ObservableFrequencyDistribution ofd = frequencyDistributionTableView.getSelectionModel().getSelectedItem();
        if (ofd != null && ofd.getValue() != null && !ofd.getValue().isEmpty()) {
            String value = ofd.getValue();
            WebView v = new WebView();
            WebEngine webEngine = v.getEngine();
            resultsScroll.setContent(v);
            webEngine.load("https://en.wikipedia.org/wiki/" + value);
        } else {
            resultsScroll.setContent(new Label(bundle.getString("view.preview.empty")));
        }
        viewMode = ViewMode.Wikipedia;
    }

    /**
     * Handles Maps action.
     */
    public final void handleViewGoogleMapsAction() {
        ObservableFrequencyDistribution ofd = frequencyDistributionTableView.getSelectionModel().getSelectedItem();
        if (ofd != null && ofd.getValue() != null && !ofd.getValue().isEmpty()) {
            String value = ofd.getValue();
            WebView v = new WebView();
            WebEngine webEngine = v.getEngine();
            resultsScroll.setContent(v);
            webEngine.load("https://www.google.com/maps?q=" + value);
        } else {
            resultsScroll.setContent(new Label(bundle.getString("view.preview.empty")));
        }
        viewMode = ViewMode.Maps;

    }

    /**
     * Handles Search action.
     */
    public final void handleViewGoogleSearchAction() {
        ObservableFrequencyDistribution ofd = frequencyDistributionTableView.getSelectionModel().getSelectedItem();
        if (ofd != null && ofd.getValue() != null && !ofd.getValue().isEmpty()) {
            String value = ofd.getValue();
            WebView v = new WebView();
            WebEngine webEngine = v.getEngine();
            resultsScroll.setContent(v);
            webEngine.load("https://www.google.no/?q=" + value);
        } else {
            resultsScroll.setContent(new Label(bundle.getString("view.preview.empty")));
        }
        viewMode = ViewMode.Search;
    }

    /**
     * Identifies the active view in the application.
     */
    enum ViewMode {
        /**
         * Shows the results.
         */
        Results,
        /**
         * Shows a chart.
         */
        Chart,
        /**
         * Shows a preview of the data.
         */
        Preview,
        /**
         * Shows the selected value in Wikipedia.
         */
        Wikipedia,
        /**
         * Shows the selected value in Google Maps.
         */
        Maps,
        /**
         * Shows the selected value in Google Search.
         */
        Search
    }


}
