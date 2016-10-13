package org.laukvik.csv.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

/**
 * JavaFX MenuBar for the CSV application.
 *
 * @author Morten Laukvik
 */
class CsvMenuBar extends MenuBar {

    final Main main;

    CsvMenuBar(final Main main) {
        super();
        this.main = main;
        setUseSystemMenuBar(true);
        // ----- File -----
        final Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(KeyCombination.keyCombination("Meta+n"));
        newItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.newFile();
            }
        });

        MenuItem openItem = new MenuItem("Open");
        openItem.setAccelerator(KeyCombination.keyCombination("Meta+o"));
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.openFileDialog();
            }
        });


        MenuItem openFileOptions = new MenuItem("Open...");
        openFileOptions.setAccelerator(KeyCombination.keyCombination("Meta+o+shift"));
        openFileOptions.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.openFileDialogWithOptions();
            }
        });

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setAccelerator(KeyCombination.keyCombination("Meta+s"));
        MenuItem saveAsItem = new MenuItem("Save as");
        saveAsItem.setAccelerator(KeyCombination.keyCombination("Meta+s+shift"));
        MenuItem exportItem = new MenuItem("Export");
        exportItem.setAccelerator(KeyCombination.keyCombination("Meta+e"));

        MenuItem printItem = new MenuItem("Print");
        printItem.setAccelerator(KeyCombination.keyCombination("Meta+p"));
        printItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handlePrintAction();
            }
        });
        fileMenu.getItems().addAll(newItem, openItem, openFileOptions, saveItem, saveAsItem, exportItem, printItem);



        // ----- Edit ------
        final Menu edit = new Menu("Edit");
        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setAccelerator(KeyCombination.keyCombination("Meta+x"));
        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setAccelerator(KeyCombination.keyCombination("Meta+c"));
        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setAccelerator(KeyCombination.keyCombination("Meta+v"));
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setAccelerator(KeyCombination.keyCombination("Meta+backspace"));
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.out.println(t);
                main.handleDeleteAction();
            }
        });
        edit.getItems().addAll(cutItem, copyItem, pasteItem, deleteItem);

        // ----- Insert ------
        final Menu insert = new Menu("Sett inn");
        MenuItem newColumnItem = new MenuItem("Kolonne");
        newColumnItem.setAccelerator(KeyCombination.keyCombination("Meta+i"));
        newColumnItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleNewColumnAction();
            }
        });
        MenuItem newRowItem = new MenuItem("Rad");
        newRowItem.setAccelerator(KeyCombination.keyCombination("Meta+R"));
        newRowItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.out.println(t);
                main.handleNewRowAction();
            }
        });
        MenuItem headersRowItem = new MenuItem("Headers");
        headersRowItem.setAccelerator(KeyCombination.keyCombination("Meta+H"));
        headersRowItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.out.println(t);
                main.handleNewHeaders();
            }
        });
        insert.getItems().addAll(newColumnItem,newRowItem,headersRowItem);

        // ----- Help ------
        final Menu help = new Menu("Help");

        //
        getMenus().add(fileMenu);
        getMenus().add(edit);
        getMenus().add(insert);
        getMenus().add(help);
    }

}
