/*
 * Copyright 2015 Laukviks Bedrifter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laukvik.csv.javafx;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.laukvik.csv.CSV;
import org.laukvik.csv.DistinctColumnValues;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

public class App extends Application {

    private CSV csv;

    private MenuBar menu;
    private Menu fileMenu;
    private Menu editMenu;
    private Menu helpMenu;

    //
    private VBox topContainer;  //Creates a container to hold all Menu Objects.
    private BorderPane root;
    private SplitPane split;
    private ScrollPane scrollAccordion;
    private ScrollPane scrollTable;
    private Accordion accordion;
    private TableView<ObservableRow> tableView;
    private ObservableList<ObservableRow> data = FXCollections.observableArrayList();
    Stage primaryStage;

    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        // Menu
        menu = new MenuBar();
        menu.setUseSystemMenuBar(true);
        // ----- File -----
        fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(KeyCombination.keyCombination("Meta+n"));
        newItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                newCSV();
            }
        });
        fileMenu.getItems().add(newItem);

        MenuItem openItem = new MenuItem("Open");
        openItem.setAccelerator(KeyCombination.keyCombination("Meta+o"));
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open CSV file");
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    openFile(file);
                }
            }
        });

        fileMenu.getItems().add(openItem);

        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setAccelerator(KeyCombination.keyCombination("Meta+q"));
        quitItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });
        fileMenu.getItems().add(quitItem);
        // ----- Edit -----
        editMenu = new Menu("Edit");
        editMenu.getItems().add(new MenuItem("Cut"));
        // ----- Help -----
        helpMenu = new Menu("Help");
        helpMenu.getItems().add(new MenuItem("About"));
        menu.getMenus().addAll(fileMenu, editMenu, helpMenu);
        //
        accordion = new Accordion();
        //
        tableView = new TableView<>();
        scrollTable = new ScrollPane(tableView);
        scrollTable.setFitToHeight(true);
        scrollTable.setFitToWidth(true);
        //
        split = new SplitPane();
        split.getItems().addAll(accordion, scrollTable);
        split.setDividerPositions(0.2);

        //
        topContainer = new VBox();
        topContainer.getChildren().add(menu);
        //
        root = new BorderPane();
        root.setTop(topContainer);
        root.setCenter(split);
        // Calculate window size
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        Float width = size.width * 0.8f;
        Float height = size.height * 0.7f;

        Scene scene = new Scene(root, width.intValue(), height.intValue());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void openFile(File file) {
        if (!file.exists()) {
        } else {
            try {
                csv = new CSV();
                csv.read(file);
                openCSV(csv);
                primaryStage.setTitle(file.getName());
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void newCSV() {
        this.csv = new CSV();
        openCSV(csv);
    }

    public void openCSV(CSV csv) {
        this.csv = csv;
        accordion.getPanes().clear();
        tableView.setItems(null);
        tableView.getColumns().clear();
        data.clear();

        for (int x = 0; x < csv.getMetaData().getColumnCount(); x++) {
            Column c = csv.getMetaData().getColumn(x);
            TableColumn<ObservableRow, String> tc = new TableColumn<>(c.getName());
            final int colX = x;
            tc.setCellValueFactory(
                    new Callback<CellDataFeatures<ObservableRow, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(CellDataFeatures<ObservableRow, String> param) {
                            return param.getValue().getValue(colX);
                        }
                    }
            );
            tc.setCellFactory(TextFieldTableCell.<ObservableRow>forTableColumn());
            tc.setMinWidth(100);
            tableView.getColumns().add(tc);
            tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

            // Accordion stuff
            TitledPane pane = new TitledPane();
            pane.setText(c.getName());

            // Find all unique
            DistinctColumnValues dcv = csv.getDistinctColumnValues(x);

            //
            List<UniqueRow> list = new ArrayList<>();
            for (String key : dcv.getKeys()) {
                list.add(new UniqueRow(key, dcv.getCount(key)));
            }

            final ObservableList<UniqueRow> data2 = FXCollections.observableArrayList(list);
            // Build table
            TableView<UniqueRow> tv = new TableView(data2);
            tv.setPlaceholder(new Label("Placeholder"));
            tv.setEditable(true);

            TableColumn selectColumn = new TableColumn("");
            selectColumn.setMinWidth(32);
            selectColumn.setMaxWidth(32);
            selectColumn.setCellValueFactory(new PropertyValueFactory<UniqueRow, Boolean>("selected"));
            selectColumn.setCellFactory(new Callback<TableColumn<UniqueRow, Boolean>, TableCell<UniqueRow, Boolean>>() {
                public TableCell<UniqueRow, Boolean> call(TableColumn<UniqueRow, Boolean> p) {
                    return new CheckBoxTableCell<UniqueRow, Boolean>();
                }
            });

            TableColumn titleColumn = new TableColumn("Value");
            titleColumn.setMinWidth(100);
            titleColumn.setCellValueFactory(new PropertyValueFactory<UniqueRow, String>("title"));

            TableColumn countColumn = new TableColumn("Count");
            countColumn.setMinWidth(64);
            countColumn.setCellValueFactory(new PropertyValueFactory<UniqueRow, Integer>("count"));

            tv.getColumns().addAll(selectColumn, titleColumn, countColumn);
            tv.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

            pane.setContent(tv);
            accordion.getPanes().add(pane);
        }
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            data.add(new ObservableRow(r));
        }
        tableView.setItems(data);
        tableView.setEditable(true);
        tableView.setManaged(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
