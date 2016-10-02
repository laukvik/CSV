package org.laukvik.csv.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.laukvik.csv.CSV;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Morten Laukvik
 */
public class ImportDialog extends Stage {

    public ImportDialog(Stage owner) {
        super();
        initOwner(owner);
        setTitle("Import file...");
        Group root = new Group();
        Scene scene = new Scene(root, 400, 300);
        setScene(scene);

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(30);
        gridpane.setVgap(30);

        final Label separatorLabel = new Label("Separator");
        gridpane.add(separatorLabel, 0, 1);
        final ChoiceBox separatorBox = new ChoiceBox();
        List<String>items = new ArrayList<>();
        items.add("auto");
        for (char c : CSV.getSupportedSeparatorChars()){
            items.add(Builder.getSeparatorString(c));
        }
        separatorBox.getItems().addAll(items);
        gridpane.add(separatorBox, 1, 1);

        final Label charsetLabel = new Label("Encoding");
        gridpane.add(charsetLabel, 0, 2);
        final ChoiceBox charsetBox = new ChoiceBox();
        charsetBox.getItems().add("auto");
        for (String key : Charset.availableCharsets().keySet()){
            charsetBox.getItems().add(key);
        }
        gridpane.add(charsetBox, 1, 2);

        final Button okButton = new Button("Ok");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                close();
            }
        });
        final Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                close();
            }
        });
        gridpane.add(okButton, 3, 3);
        gridpane.add(cancelButton, 2, 3);
        GridPane.setHalignment(okButton, HPos.RIGHT);
        root.getChildren().add(gridpane);
    }



}
