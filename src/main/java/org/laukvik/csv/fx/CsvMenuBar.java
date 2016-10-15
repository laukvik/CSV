package org.laukvik.csv.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;

import java.io.File;
import java.util.ResourceBundle;

/**
 * JavaFX MenuBar for the CSV application.
 *
 * @author Morten Laukvik
 */
class CsvMenuBar extends MenuBar {

    private final Main main;
    private final Menu openRecentMenu;
    private final ResourceBundle bundle;

    CsvMenuBar(final Main main) {
        super();
        this.main = main;
        bundle = Builder.getBundle();
        setUseSystemMenuBar(true);
        // ----- File -----
        final Menu fileMenu = new Menu(bundle.getString("file"));
        MenuItem newItem = new MenuItem(bundle.getString("file.new"));
        newItem.setAccelerator(KeyCombination.keyCombination("Meta+n"));
        newItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.newFile();
            }
        });

        MenuItem openItem = new MenuItem(bundle.getString("file.open"));
        openItem.setAccelerator(KeyCombination.keyCombination("Meta+o"));
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.openFileDialog();
            }
        });

        // ------ Recent files ------
        openRecentMenu = new Menu(bundle.getString("file.recent"));

        MenuItem saveItem = new MenuItem(bundle.getString("file.save"));
        saveItem.setAccelerator(KeyCombination.keyCombination("Meta+s"));
        MenuItem saveAsItem = new MenuItem(bundle.getString("file.saveas"));
        saveAsItem.setAccelerator(KeyCombination.keyCombination("Meta+s+shift"));

        MenuItem importItem = new MenuItem(bundle.getString("file.import"));
        importItem.setAccelerator(KeyCombination.keyCombination("Meta+i"));
        importItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.openFileDialogWithOptions();
            }
        });


        final Menu exportMenu = new Menu(bundle.getString("file.export"));

        MenuItem exportJsonItem = new MenuItem(bundle.getString("file.export.json"));
        MenuItem exportXmlItem = new MenuItem(bundle.getString("file.export.xml"));
        MenuItem exportCsvItem = new MenuItem(bundle.getString("file.export.html"));
        exportJsonItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleExportJsonAction();
            }
        });
        exportXmlItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleExportXmlAction();
            }
        });
        exportCsvItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleExportHtmlAction();
            }
        });

        exportMenu.getItems().addAll(exportJsonItem,exportXmlItem,exportCsvItem);

        MenuItem printItem = new MenuItem(bundle.getString("file.print"));
        printItem.setAccelerator(KeyCombination.keyCombination("Meta+p"));
        printItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handlePrintAction();
            }
        });
        fileMenu.getItems().addAll(newItem, openItem, openRecentMenu, saveItem, saveAsItem, new SeparatorMenuItem(),
                importItem, exportMenu, new SeparatorMenuItem(), printItem);



        // ----- Edit ------
        final Menu edit = new Menu(bundle.getString("edit"));
        MenuItem cutItem = new MenuItem(bundle.getString("edit.cut"));
        cutItem.setAccelerator(KeyCombination.keyCombination("Meta+x"));
        cutItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleCutAction();
            }
        });

        MenuItem copyItem = new MenuItem(bundle.getString("edit.copy"));
        copyItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleCopyAction();
            }
        });
        copyItem.setAccelerator(KeyCombination.keyCombination("Meta+c"));
        MenuItem pasteItem = new MenuItem(bundle.getString("edit.paste"));
        pasteItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handlePasteAction();
            }
        });
        pasteItem.setAccelerator(KeyCombination.keyCombination("Meta+v"));
        MenuItem deleteItem = new MenuItem(bundle.getString("edit.delete"));
        deleteItem.setAccelerator(KeyCombination.keyCombination("Meta+backspace"));
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleDeleteAction();
            }
        });

        MenuItem moveUpItem = new MenuItem(bundle.getString("edit.moveup"));
        moveUpItem.setAccelerator(KeyCombination.keyCombination("Meta+"+ KeyCode.UP));
        moveUpItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleUpAction();
            }
        });
        MenuItem moveDownItem = new MenuItem(bundle.getString("edit.movedown"));
        moveDownItem.setAccelerator(KeyCombination.keyCombination("Meta+"+ KeyCode.DOWN));
        moveDownItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleDownAction();
            }
        });
        edit.getItems().addAll(cutItem, copyItem, pasteItem, deleteItem, new SeparatorMenuItem(), moveUpItem, moveDownItem);

        // ----- Insert ------
        final Menu insert = new Menu(bundle.getString("insert"));
        MenuItem newColumnItem = new MenuItem(bundle.getString("insert.column"));
        newColumnItem.setAccelerator(KeyCombination.keyCombination("Meta+i"));
        newColumnItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleNewColumnAction();
            }
        });
        MenuItem newRowItem = new MenuItem(bundle.getString("insert.row"));
        newRowItem.setAccelerator(KeyCombination.keyCombination("Meta+R"));
        newRowItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleNewRowAction();
            }
        });
        MenuItem headersRowItem = new MenuItem(bundle.getString("insert.headers"));
        headersRowItem.setAccelerator(KeyCombination.keyCombination("Meta+H"));
        headersRowItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleNewHeaders();
            }
        });
        insert.getItems().addAll(newColumnItem,newRowItem,headersRowItem);

        // ----- Help ------
        final Menu help = new Menu(bundle.getString("help"));

        //
        getMenus().add(fileMenu);
        getMenus().add(edit);
        getMenus().add(insert);
        getMenus().add(help);
    }

    public void buildRecentList(Recent recent){
        openRecentMenu.getItems().clear();
        for (File file : recent.getList()){
            MenuItem openRecentItem = new MenuItem(file.getAbsolutePath());
            openRecentItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    main.loadFile(file, null, null);
                }
            });
            openRecentMenu.getItems().add(openRecentItem);
        }
        openRecentMenu.getItems().add( new SeparatorMenuItem());
        MenuItem openRecentItem = new MenuItem(bundle.getString("file.recent.clear"));
        openRecentItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                recent.clear();
                buildRecentList(recent);
            }
        });
        openRecentMenu.getItems().add(openRecentItem);
    }


}
