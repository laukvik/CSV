package org.laukvik.csv.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
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

    /**
     * The Menu for View
     */
    private final Menu viewMenu;
    /**
     * The Application this MenuBar belongs to.
     */
    private final Main main;
    /**
     * The Menu for recent files.
     */
    private final Menu openRecentMenu;
    /** The ResourceBundle. */
    private final ResourceBundle bundle;

    CsvMenuBar(final Main main) {
        super();
        this.main = main;
        bundle = Builder.getBundle();
        setUseSystemMenuBar(Builder.isMac());
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
                main.handleFileOpen();
            }
        });

        // ------ Recent files ------
        openRecentMenu = new Menu(bundle.getString("file.recent"));

        MenuItem saveItem = new MenuItem(bundle.getString("file.save"));
        saveItem.setAccelerator(KeyCombination.keyCombination("Meta+s"));
        MenuItem saveAsItem = new MenuItem(bundle.getString("file.saveas"));
        saveAsItem.setAccelerator(KeyCombination.keyCombination("Meta+s+shift"));
        saveAsItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleSaveAsAction();
            }
        });

        MenuItem importItem = new MenuItem(bundle.getString("file.import"));
        importItem.setAccelerator(KeyCombination.keyCombination("Meta+i"));
        importItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleOpenFileWithOptions();
            }
        });


        final Menu exportMenu = new Menu(bundle.getString("file.export"));

        MenuItem exportJsonItem = new MenuItem(bundle.getString("file.export.json"));
        MenuItem exportXmlItem = new MenuItem(bundle.getString("file.export.xml"));
        MenuItem exportHtmlItem = new MenuItem(bundle.getString("file.export.html"));
        MenuItem resourceBundleItem = new MenuItem(bundle.getString("file.export.resourcebundle"));
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
        exportHtmlItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleExportHtmlAction();
            }
        });
        resourceBundleItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleOpenResourceBundleAction();
            }
        });

        exportMenu.getItems().addAll(exportJsonItem,exportXmlItem,exportHtmlItem, resourceBundleItem);

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
                main.handleMoveUpAction();
            }
        });
        MenuItem moveDownItem = new MenuItem(bundle.getString("edit.movedown"));
        moveDownItem.setAccelerator(KeyCombination.keyCombination("Meta+"+ KeyCode.DOWN));
        moveDownItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleMoveDownAction();
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
        insert.getItems().addAll(newColumnItem, newRowItem, headersRowItem);



        // ----- Query ------
        final Menu queryMenu = new Menu(bundle.getString("query"));  // Clear query
        MenuItem newQueryMenuItem = new MenuItem(bundle.getString("query.new"));
        newQueryMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleNewQuery();
            }
        });

        queryMenu.getItems().addAll(newQueryMenuItem);

        // ----- View ------
        viewMenu = new Menu(bundle.getString("view"));
        CheckMenuItem viewResultsMenuItem = new CheckMenuItem(bundle.getString("view.results"));
        viewResultsMenuItem.setAccelerator(KeyCombination.keyCombination("Meta+1"));
        viewResultsMenuItem.setSelected(true);
        viewResultsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                setSelectedMode(0);
                main.handleViewResultsAction();
            }
        });

        CheckMenuItem viewChartMenuItem = new CheckMenuItem(bundle.getString("view.piechart"));
        viewChartMenuItem.setAccelerator(KeyCombination.keyCombination("Meta+2"));
        viewChartMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                setSelectedMode(1);
                main.handleViewChartAction();

            }
        });

        CheckMenuItem previewChartMenuItem = new CheckMenuItem(bundle.getString("view.preview"));
        previewChartMenuItem.setAccelerator(KeyCombination.keyCombination("Meta+3"));
        previewChartMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                setSelectedMode(2);
                main.handleViewPreviewAction();

            }
        });

        CheckMenuItem wikipediaMenuItem = new CheckMenuItem(bundle.getString("view.wikipedia"));
        wikipediaMenuItem.setAccelerator(KeyCombination.keyCombination("Meta+4"));
        wikipediaMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                setSelectedMode(3);
                main.handleViewWikipediaAction();

            }
        });

        CheckMenuItem googleMapsMenuItem = new CheckMenuItem(bundle.getString("view.googlemaps"));
        googleMapsMenuItem.setAccelerator(KeyCombination.keyCombination("Meta+5"));
        googleMapsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                setSelectedMode(4);
                main.handleViewGoogleMapsAction();

            }
        });

        CheckMenuItem googleSearchMenuItem = new CheckMenuItem(bundle.getString("view.google"));
        googleSearchMenuItem.setAccelerator(KeyCombination.keyCombination("Meta+6"));
        googleSearchMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                setSelectedMode(5);
                main.handleViewGoogleSearchAction();

            }
        });

        viewMenu.getItems().addAll(viewResultsMenuItem, viewChartMenuItem, previewChartMenuItem, wikipediaMenuItem, googleMapsMenuItem, googleSearchMenuItem);

        // ----- Help ------
        final Menu help = new Menu(bundle.getString("help"));
        MenuItem aboutMenuItem = new MenuItem(bundle.getString("help.about"));
        aboutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                main.handleAboutAction();
            }
        });
        help.getItems().addAll(aboutMenuItem);

        //
        getMenus().add(fileMenu);
        getMenus().add(edit);
        getMenus().add(queryMenu);
        getMenus().add(insert);
        getMenus().addAll(viewMenu);
        getMenus().add(help);
    }

    public void buildRecentList(Recent recent){
        openRecentMenu.getItems().clear();
        for (File file : recent.getList()){
            MenuItem openRecentItem = new MenuItem(file.getAbsolutePath());
            openRecentItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    main.loadFile(file);
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

    private void setSelectedMode(int index) {
        int x = 0;
        for (MenuItem item : viewMenu.getItems()) {
            CheckMenuItem i = (CheckMenuItem) item;
            i.setSelected(x == index);
            x++;
        }
    }

}
