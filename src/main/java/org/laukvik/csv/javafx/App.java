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

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.laukvik.csv.CSV;
import org.laukvik.csv.ParseException;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

public class App extends Application {

    private TableView<ObservableRow> tableView;
    private CSV csv;
    private ObservableList<ObservableRow> data = FXCollections.observableArrayList();
    private MenuBar menu;
    private final Menu fileMenu = new Menu("File");
    private final Menu editMenu = new Menu("Edit");
    private final Menu helpMenu = new Menu("Help");

    BorderPane root = new BorderPane();
    VBox topContainer = new VBox();  //Creates a container to hold all Menu Objects.
    ToolBar toolBar = new ToolBar();  //Creates our tool-bar to hold the buttons.

    @Override
    public void start(Stage primaryStage) {
        tableView = new TableView<>();
        //
        fileMenu.getItems().add(new MenuItem("Edit"));
        menu = new MenuBar();
        menu.setUseSystemMenuBar(true);
        menu.getMenus().addAll(fileMenu, editMenu, helpMenu);

//        StackPane root = new StackPane();
        topContainer.getChildren().add(menu);
//        topContainer.getChildren().add(toolBar);

        root.setTop(topContainer);
        root.setBottom(tableView);

//        root.getChildren().add(menu);
//        root.getChildren().add(tableView);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        openFile(new File("/Users/morten/Downloads/Presidents.csv"));
        primaryStage.show();
    }

    public void openFile(File file) {
        if (!file.exists()) {
        } else {
            try {
                csv = new CSV(file);
                openCSV(csv);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void openCSV(CSV csv) {
        this.csv = csv;
        System.out.println("Table: " + tableView);
        tableView.getColumns().clear();
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

            tc.setMinWidth(100);
            tableView.getColumns().add(tc);
        }

        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            data.add(new ObservableRow(r));
        }

        tableView.setItems(data);
    }

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        launch(args);
    }
}
