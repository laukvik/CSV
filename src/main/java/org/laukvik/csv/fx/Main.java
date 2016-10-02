package org.laukvik.csv.fx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.laukvik.csv.CSV;
import org.laukvik.csv.ChangeListener;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.laukvik.csv.fx.Builder.buildColumnsTable;
import static org.laukvik.csv.fx.Builder.buildMenuBar;
import static org.laukvik.csv.fx.Builder.buildUniqueTable;
import static org.laukvik.csv.fx.Builder.createColumnsObservableList;
import static org.laukvik.csv.fx.Builder.createResultsColumns;
import static org.laukvik.csv.fx.Builder.createResultsRows;
import static org.laukvik.csv.fx.Builder.createUniqueObservableList;
import static org.laukvik.csv.fx.Builder.getPercentSize;


/**
 * @author Morten Laukvik
 */
public class Main extends Application implements ChangeListener {

    private ResourceBundle bundle = Builder.getBundle();
    private CSV csv;
    private Stage stage;
    private TableView<ObservableColumn> columnsTableView;
    private TableView<ObservableUnique> uniqueTableView;
    private TableView<ObservableRow> resultsTableView;
    private int selectedColumnIndex;
    private Label rowsLabel;
    private Label colsLabel;
    private Label encodingLabel;
    private Label sizeLabel;
    private Label separatorLabel;
    private ProgressBar progressBar;


    public void setSelectedColumnIndex(int selectedColumnIndex){
        this.selectedColumnIndex = selectedColumnIndex;
//        columnsTableView.getSelectionModel().select(selectedColumnIndex);
//        columnsTableView.getFocusModel().focus(selectedColumnIndex);
        if (selectedColumnIndex > -1){
            uniqueTableView.setItems(createUniqueObservableList(selectedColumnIndex, csv));
        }

    }

    public void openFileDialog(){
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open .CSV file");
        final File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null){
            loadFile(selectedFile);
        }
    }

    public void newFile() {
        stage.setTitle("");
        csv = new CSV();
        csv.addChangeListener(this);
        selectedColumnIndex = 0;
        columnsTableView.setItems(FXCollections.observableArrayList());
        uniqueTableView.setItems(FXCollections.observableArrayList());
        resultsTableView.setItems(FXCollections.observableArrayList());
        resultsTableView.getColumns().clear();
    }


    public void loadFile(File file){
        newFile();
        try {
            csv.readFile(file);
            stage.setTitle(file.getAbsolutePath());
            setSelectedColumnIndex(0);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("app.title"));
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        columnsTableView = buildColumnsTable();
        uniqueTableView = buildUniqueTable();
        resultsTableView = new TableView<>();
        resultsTableView.setEditable(true);

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
        topContainer.getChildren().add(buildMenuBar(this));

        final ToolBar bar = new ToolBar();
        Label rows = new Label("Rows: ");
        rowsLabel = new Label("-");
        Label cols = new Label("Cols: ");
        colsLabel = new Label("-");
        Label encoding = new Label("Encoding: ");
        encodingLabel = new Label("-");
        Label size = new Label("Size: ");
        sizeLabel = new Label("-");
        Label separator = new Label("Separator: ");
        separatorLabel = new Label("-");
        progressBar = new ProgressBar();
        progressBar.setVisible(false);
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


    public void updateToolbar(){
        rowsLabel.setText(csv.getRowCount() + "");
        colsLabel.setText(csv.getMetaData().getColumnCount() + "");
        encodingLabel.setText(csv.getRowCount() + "");
        sizeLabel.setText(csv.getFile().getName() + "");
        separatorLabel.setText(csv.getMetaData().getSeparatorChar() + "");
    }

    public void alert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("app.title"));
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @Override
    public void columnCreated(final Column column) {
//        columnsTableView.setItems(createColumnsObservableList(csv.getMetaData()));
        updateColumns();
        updateRows();
        updateToolbar();
        alert("columnCreated: " + column.getName());
    }

    @Override
    public void columnUpdated(final Column column) {
        resultsTableView.getColumns().get(column.indexOf()).setText(column.getName());
        updateColumns();
//        columnsTableView.getSelectionModel().select(selectedColumnIndex);
//        columnsTableView.getFocusModel().focus(selectedColumnIndex);
        updateRows();
//        alert("columnUpdated: " + column.getName());
    }

    @Override
    public void columnRemoved(final int columnIndex) {
//        updateColumns();
//        resultsTableView.getColumns().remove(columnIndex);
//        columnsTableView.getItems().remove(columnIndex);
//        updateRows();
//        alert("columnRemoved: " + column.getName());
        updateToolbar();
    }

    @Override
    public void rowUpdated(final int rowIndex, final Row row) {
//        updateColumns();
//        updateRows();
//        alert("rowUpdated: " + row);
    }

    @Override
    public void rowRemoved(final int rowIndex, final Row row) {
        resultsTableView.getItems().remove(rowIndex);
//        updateRows();
//        alert("rowRemoved: " + row);
        updateToolbar();
    }

    @Override
    public void rowCreated(final int rowIndex, final Row row) {
//        alert("rowCreated: " + rowIndex);
        if (rowIndex == resultsTableView.getItems().size()+1){
            resultsTableView.getItems().add(new ObservableRow(row));
        } else {
            resultsTableView.getItems().add(rowIndex, new ObservableRow(row));
        }
        updateToolbar();
    }

    @Override
    public void metaDataRead(final MetaData metaData) {
        columnsTableView.setItems(createColumnsObservableList(metaData));
        createResultsColumns(resultsTableView, metaData);
    }

    @Override
    public void cellUpdated(final int columnIndex, final int rowIndex) {
        alert("cellUpdated: " + columnIndex + "x" + rowIndex );
    }

    @Override
    public void beginRead(final File file) {
        progressBar.setVisible(true);
        alert("beginRead: " + file.getName());
    }

    @Override
    public void finishRead(final File file) {
        progressBar.setVisible(false);
        alert("finishRead: " + file.getName());
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
        alert("handleUnique: " + u.getValue());
    }

    public void handleDeleteColumn(int columnIndex){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("app.title"));
        alert.setHeaderText("Slett kolonne");
        alert.setContentText("Er du sikker på at du vil slette kolonnen " + csv.getMetaData().getColumn(columnIndex).getName() + "?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            deleteColumn(columnIndex);
        }
    }

    private void updateColumns(){
        columnsTableView.setItems(createColumnsObservableList(csv.getMetaData()));
        createResultsColumns(resultsTableView, csv.getMetaData());
    }

    private void updateRows(){
        createResultsColumns(resultsTableView, csv.getMetaData());
        createResultsRows(resultsTableView, csv);
    }

    public void deleteColumn(final int columnIndex){
        csv.removeColumn(csv.getMetaData().getColumn(columnIndex));
//        columnsTableView.setItems(createColumnsObservableList(csv.getMetaData()));
//        createResultsColumns(resultsTableView, csv.getMetaData());
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
    }

    public void handlePrintAction(){
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob.showPrintDialog(stage) && printerJob.printPage(resultsTableView)) {
            printerJob.endJob();
            alert("Finished printing.");
        }
    }

    public void handleNewColumnAction() {
        System.out.println("handleNewColumnAction");
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(bundle.getString("app.title"));
        dialog.setHeaderText("Ny kolonne");
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
//        alert("handleNewRowAction");
    }

    /**
     *
     */
    public void handleNewHeaders() {
        csv.insertHeaders();
        updateRows();
    }
}
