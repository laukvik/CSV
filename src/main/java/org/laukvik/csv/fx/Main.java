package org.laukvik.csv.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.laukvik.csv.fx.Builder.createAllObservableList;
import static org.laukvik.csv.fx.Builder.createResultsColumns;
import static org.laukvik.csv.fx.Builder.createResultsRows;
import static org.laukvik.csv.fx.Builder.createUniqueObservableList;
import static org.laukvik.csv.fx.Builder.getPercentSize;
import static org.laukvik.csv.fx.Builder.getSeparatorCharByString;
import static org.laukvik.csv.fx.Builder.getSeparatorString;
import static org.laukvik.csv.fx.Builder.toKb;


/**
 * @author Morten Laukvik
 */
public class Main extends Application implements ChangeListener, FileListener {

    private final ResourceBundle bundle = Builder.getBundle();
    private CSV csv;
    private Stage stage;
    private ColumnsTableView columnsTableView;
    private UniqueTableView uniqueTableView;
    private ResultsTableView resultsTableView;
    private Label rowsLabel;
    private Label colsLabel;
    private Label encodingLabel;
    private Label sizeLabel;
    private Label separatorLabel;
    private ProgressBar progressBar;

    public static void main(String[] args) {
        launch(args);
    }

    public static String toClipboardString(final int rowIndex, final CSV csv) {
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

    @Override
    public void start(final Stage primaryStage ) throws Exception {
        this.stage = primaryStage;
        columnsTableView = new ColumnsTableView();
        uniqueTableView = new UniqueTableView();
        resultsTableView = new ResultsTableView();

        final ScrollPane columnsScroll = new ScrollPane(columnsTableView);
        columnsScroll.setFitToHeight(true);
        columnsScroll.setFitToWidth(true);

        final ScrollPane uniqueScroll = new ScrollPane(uniqueTableView);
        uniqueScroll.setFitToHeight(true);
        uniqueScroll.setFitToWidth(true);

        columnsTableView.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
            @Override
            public void onChanged(final Change<? extends Integer> c) {
                int rowIndex = columnsTableView.getSelectionModel().getSelectedIndex();
                setSelectedColumnIndex(rowIndex);
            }
        });

        final ScrollPane resultsScroll = new ScrollPane(resultsTableView);
        resultsScroll.setFitToHeight(true);
        resultsScroll.setFitToWidth(true);

        final SplitPane tableSplit = new SplitPane(columnsScroll, uniqueScroll);
        tableSplit.setOrientation(Orientation.VERTICAL);
        tableSplit.setDividerPosition(0, 0.25);
        final SplitPane mainSplit = new SplitPane(tableSplit, resultsScroll);
        mainSplit.setDividerPositions(0.2);

        final VBox topContainer = new VBox();
        topContainer.getChildren().add(new CsvMenuBar(this));

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
        bar.getItems().addAll(rows, rowsLabel, cols, colsLabel, size, sizeLabel, encoding, encodingLabel, separator, separatorLabel, progressBar);

        final BorderPane root = new BorderPane();
        root.setTop(topContainer);
        root.setCenter(mainSplit);
        root.setBottom(bar);

        final java.awt.Dimension percent = getPercentSize( 0.8f, 0.7f );
        final Scene scene = new Scene(root, percent.getWidth(), percent.getHeight() );
        stage.setScene(scene);
        stage.show();
        newFile();
    }

    public void setSelectedColumnIndex(int selectedColumnIndex){
        if (selectedColumnIndex > -1){
            uniqueTableView.setItems(createUniqueObservableList(selectedColumnIndex, csv));
        }
    }

    public void openFileDialog(){
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.open"));
        final File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null){
            loadFile(selectedFile, null, null);
        }
    }

    public void openFileDialogWithOptions() {
        final Dialog dialog = new Dialog( );
        dialog.setTitle(bundle.getString("app.title"));
        dialog.setHeaderText(bundle.getString("dialog.file.open"));

        final GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(10,20,10,20));
        gridpane.setHgap(20);
        gridpane.setVgap(10);

        final Label separatorLabel = new Label(bundle.getString("metadata.separator"));
        gridpane.add(separatorLabel, 0, 1);
        final ChoiceBox separatorBox = new ChoiceBox();
        List<String> items = new ArrayList<>();
        items.add(bundle.getString("metadata.separator.autodetect"));
        for (char c : CSV.getSupportedSeparatorChars()){
            items.add(Builder.getSeparatorString(c));
        }
        separatorBox.getItems().addAll(items);
        gridpane.add(separatorBox, 1, 1);
        separatorBox.getSelectionModel().select(0);

        final Label charsetLabel = new Label(bundle.getString("metadata.encoding"));
        gridpane.add(charsetLabel, 0, 2);
        final ChoiceBox charsetBox = new ChoiceBox();
        charsetBox.getItems().add(bundle.getString("metadata.encoding.autodetect"));
        for (BOM b : BOM.values()){
            charsetBox.getItems().add(b.name());
        }
        for (String key : Charset.availableCharsets().keySet()){
            charsetBox.getItems().add(key);
        }
        charsetBox.getSelectionModel().select(0);
        gridpane.add(charsetBox, 1, 2);
        dialog.getDialogPane().setContent(gridpane);

        // Set the button types.
        ButtonType okButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);
        dialog.showAndWait();
        ButtonType resultButtonType = (ButtonType) dialog.getResult();

        if (resultButtonType.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)){
            Character separator = separatorBox.getSelectionModel().getSelectedIndex() == 0 ? null : getSeparatorCharByString((String)separatorBox.getSelectionModel().getSelectedItem());
            Charset charset = charsetBox.getSelectionModel().getSelectedIndex() == 0 ? null : Charset.forName((String)charsetBox.getSelectionModel().getSelectedItem());
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(bundle.getString("dialog.file.open"));
            final File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null){
                loadFile(selectedFile, separator, charset);
            }
        }
    }

    public void newFile() {
        csv = new CSV();
        csv.addChangeListener(this);
        csv.addFileListener(this);
        columnsTableView.setItems(FXCollections.observableArrayList());
        uniqueTableView.setItems(FXCollections.observableArrayList());
        resultsTableView.clearRows();
        updateToolbar();
    }

    public void loadFile(File file, Character separatorChar, Charset charset){
        newFile();
        try {
            if (charset == null && separatorChar == null){
                csv.readFile(file);
            } else if (charset != null){
                csv.readFile(file, charset, separatorChar);
            } else if (separatorChar != null){
                csv.readFile(file, separatorChar);
            }
        } catch (IOException e) {
            alert(e.getMessage());
        }
    }

    public void updateToolbar(){
        rowsLabel.setText(csv.getRowCount() + "");
        colsLabel.setText(csv.getMetaData().getColumnCount() + "");
        encodingLabel.setText(csv.getMetaData().getCharset() == null ? bundle.getString("metadata.encoding.na") : csv.getMetaData().getCharset().name());
        sizeLabel.setText(toKb(csv.getFile() == null ? 0 : csv.getFile().length()) + "");
        separatorLabel.setText(csv.getMetaData().getSeparatorChar() == null ? "" : getSeparatorString(csv.getMetaData().getSeparatorChar()));
        stage.setTitle(csv.getFile() == null ? "" : csv.getFile().getName() + "");
    }

    public void alert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("app.title"));
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @Override
    public void columnCreated(final Column column) {
        buildResultsTable();
        updateToolbar();
    }

    @Override
    public void columnUpdated(final Column column) {
        buildResultsTable();
        updateToolbar();
    }

    @Override
    public void columnRemoved(final int columnIndex) {
        buildResultsTable();
        updateToolbar();
    }

    @Override
    public void rowUpdated(final int rowIndex, final Row row) {
    }

    @Override
    public void rowRemoved(final int rowIndex, final Row row) {
        resultsTableView.getItems().remove(rowIndex);
        updateToolbar();
    }

    @Override
    public void rowCreated(final int rowIndex, final Row row) {
        if (rowIndex == resultsTableView.getItems().size()+1){
            resultsTableView.getItems().add(new ObservableRow(row));
        } else {
            resultsTableView.getItems().add(rowIndex, new ObservableRow(row));
        }
        updateToolbar();
    }

    @Override
    public void rowMoved(final int fromRowIndex, final int toRowIndex) {
        Collections.swap(resultsTableView.getItems(), fromRowIndex, toRowIndex);
    }

    @Override
    public void metaDataRead(final MetaData metaData) {
        columnsTableView.setItems(createAllObservableList(metaData));
        createResultsColumns(resultsTableView, metaData);
    }

    @Override
    public void cellUpdated(final int columnIndex, final int rowIndex) {
        alert("cellUpdated: " + columnIndex + "x" + rowIndex );
    }

    @Override
    public void beginRead(final File file) {
        progressBar.setVisible(true);
    }

    @Override
    public void finishRead(final File file) {
        progressBar.setVisible(false);
        stage.setTitle(file.getAbsolutePath());
        setSelectedColumnIndex(0);
    }

    @Override
    public void readBytes(final long count, final long total) {
        Platform.runLater(() -> progressBar.setProgress(count/(total*1f)));
    }

    @Override
    public void beginWrite(final File file) {
        alert("beginWrite: " + file.getName());
    }

    @Override
    public void finishWrite(final File file) {
        alert("finishWrite: " + file.getName());
    }

    public void handleDeleteAction() {
        Node owner = stage.getScene().getFocusOwner();
        if (owner == resultsTableView){
            handleDeleteRow(resultsTableView.getSelectionModel().getSelectedIndex());
        } else if (owner == columnsTableView){
            handleDeleteColumn(columnsTableView.getSelectionModel().getSelectedIndex());
        } else if (owner == uniqueTableView){
            handleDeleteUnique(uniqueTableView.getSelectionModel().getSelectedIndex());
        }
    }

    public void handleDeleteUnique(int columnIndex){
        ObservableUnique u = uniqueTableView.getItems().get(columnIndex);
    }

    public void handleDeleteColumn(int columnIndex){
        MessageFormat format = new MessageFormat(bundle.getString("dialog.deletecolumn.confirm"));
        Object[] messageArguments = {
                csv.getMetaData().getColumn(columnIndex).getName()
        };
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("app.title"));
        alert.setHeaderText(bundle.getString("dialog.deletecolumn"));
        alert.setContentText(format.format(messageArguments));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            deleteColumn(columnIndex);
        }
    }

    public void buildResultsTable(){
        resultsTableView.columnsChanged(csv);
    }

    private void updateColumns(){
        columnsTableView.setItems(createAllObservableList(csv.getMetaData()));
        createResultsColumns(resultsTableView, csv.getMetaData());
    }

    private void updateRows(){
        createResultsColumns(resultsTableView, csv.getMetaData());
        createResultsRows(resultsTableView, csv);
    }

    public void deleteColumn(final int columnIndex){
        csv.removeColumn(csv.getMetaData().getColumn(columnIndex));
        updateColumns();
        updateRows();
        int columnCount = csv.getMetaData().getColumnCount();
        if (columnCount == 0){
            // Nothing to select
        } else if (columnIndex > columnCount-1){
            // Deleted last
            columnsTableView.getSelectionModel().select(columnCount-1);
            columnsTableView.getFocusModel().focus(columnCount-1);
        } else if (columnIndex < columnCount ){
            columnsTableView.getSelectionModel().select(columnIndex);
            columnsTableView.getFocusModel().focus(columnIndex);
        }
    }

    public void handleDeleteRow(int rowIndex){
        csv.removeRow(rowIndex);
        resultsTableView.getSelectionModel().select(rowIndex);
    }

    public void handlePrintAction(){
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob.showPrintDialog(stage) && printerJob.printPage(resultsTableView)) {
            printerJob.endJob();
            alert(bundle.getString("dialog.print.finished"));
        }
    }

    public void handleNewColumnAction() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(bundle.getString("app.title"));
        dialog.setHeaderText(bundle.getString("dialog.newcolumn"));
        dialog.setContentText(bundle.getString("dialog.newcolumn.columnname"));
        dialog.setGraphic(Builder.getImage());

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            csv.addColumn(new StringColumn(result.get()));
        }
    }

    public void handleNewRowAction() {
        int rowIndex = resultsTableView.getSelectionModel().getSelectedIndex();
        if (rowIndex == -1){
            csv.addRow();
        } else {
            csv.addRow(rowIndex);
        }
    }

    /**
     *
     */
    public void handleNewHeaders() {
        csv.insertHeaders();
        updateRows();
    }

    public void handleCopyAction() {
        int rowIndex = resultsTableView.getSelectionModel().getSelectedIndex();
        if (rowIndex > -1) {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(toClipboardString(rowIndex, csv));
            clipboard.setContent(content);
        }
    }

    public void handlePasteAction() {
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

    public void handleCutAction() {
        int rowIndex = resultsTableView.getSelectionModel().getSelectedIndex();
        if (rowIndex > -1) {
            handleCopyAction();
            csv.removeRow(rowIndex);
            resultsTableView.getItems().remove(rowIndex);
            resultsTableView.getSelectionModel().select(rowIndex);
        }
    }

    public void handleUpAction() {
        int rowIndex = resultsTableView.getSelectionModel().getSelectedIndex();
        if (rowIndex > 0){
            csv.moveRow(rowIndex, rowIndex-1);
            resultsTableView.getSelectionModel().select(rowIndex-1);
        }
    }

    public void handleDownAction() {
        int rowIndex = resultsTableView.getSelectionModel().getSelectedIndex();
        if (rowIndex < csv.getRowCount()){
            csv.moveRow(rowIndex, rowIndex+1);
            resultsTableView.getSelectionModel().select(rowIndex+1);
        }
    }
}
