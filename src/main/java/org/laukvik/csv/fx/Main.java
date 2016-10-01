package org.laukvik.csv.fx;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.laukvik.csv.CSV;
import org.laukvik.csv.ChangeListener;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.laukvik.csv.fx.Builder.buildColumnsTable;
import static org.laukvik.csv.fx.Builder.buildMenuBar;
import static org.laukvik.csv.fx.Builder.buildUniqueTable;
import static org.laukvik.csv.fx.Builder.createColumnsObservableList;
import static org.laukvik.csv.fx.Builder.createUniqueObservableList;
import static org.laukvik.csv.fx.Builder.fillData;
import static org.laukvik.csv.fx.Builder.getPercentSize;

/**
 * @author Morten Laukvik
 */
public class Main extends Application implements ChangeListener {

    private CSV csv;
    private Stage stage;
    private TableView<ObservableColumn> columnsTableView;
    private TableView<ObservableUnique> uniqueTableView;
    private TableView<ObservableRow> resultsTableView;
    private int selectedColumnIndex;


    public void setCSV(CSV csv){
        this.csv = csv;
        selectedColumnIndex = 0;
        columnsTableView.setItems(createColumnsObservableList(csv));
        fillData(resultsTableView, csv);
        uniqueTableView.setItems(createUniqueObservableList(selectedColumnIndex, csv));
    }

    public void setSelectedColumnIndex(int selectedColumnIndex){
        this.selectedColumnIndex = selectedColumnIndex;
    }

    public void loadFile(File file){
        csv = new CSV();
//        csv.addChangeListener(this);
        try {
            csv.readFile(file);
            setCSV(csv);
            stage.setTitle(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        this.stage = primaryStage;

        columnsTableView = buildColumnsTable();
        uniqueTableView = buildUniqueTable();
        resultsTableView = new TableView<>();

        final ScrollPane columnsScroll = new ScrollPane(columnsTableView);
        columnsScroll.setFitToHeight(true);
        columnsScroll.setFitToWidth(true);

        final ScrollPane uniqueScroll = new ScrollPane(uniqueTableView);
        uniqueScroll.setFitToHeight(true);
        uniqueScroll.setFitToWidth(true);

        final ScrollPane resultsScroll = new ScrollPane(resultsTableView);
        resultsScroll.setFitToHeight(true);
        resultsScroll.setFitToWidth(true);

        final SplitPane tableSplit = new SplitPane(columnsScroll, uniqueScroll);
        tableSplit.setOrientation(Orientation.VERTICAL);
        final SplitPane mainSplit = new SplitPane(tableSplit, resultsScroll);
        mainSplit.setDividerPositions(0.2);

        final VBox topContainer = new VBox();
        topContainer.getChildren().add(buildMenuBar());

        final BorderPane root = new BorderPane();
        root.setTop(topContainer);
        root.setCenter(mainSplit);

        final Dimension percent = getPercentSize( 0.8f, 0.7f );
        final Scene scene = new Scene(root, percent.getWidth(), percent.getHeight() );
        stage.setScene(scene);
        stage.show();

        loadFile( new File("/Users/morten/Projects/LaukvikCSV/src/test/resources/presidents.csv") );
    }

    @Override
    public void columnCreated(final Column column) {

    }

    @Override
    public void columnUpdated(final Column column) {

    }

    @Override
    public void columnRemoved(final Column column) {

    }

    @Override
    public void rowUpdated(final int rowIndex, final Row row) {

    }

    @Override
    public void rowRemoved(final int rowIndex, final Row row) {

    }

    @Override
    public void rowCreated(final int rowIndex, final Row row) {

    }

    @Override
    public void metaDataRead(final MetaData metaData) {

    }
}
