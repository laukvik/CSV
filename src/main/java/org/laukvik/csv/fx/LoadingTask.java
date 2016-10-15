package org.laukvik.csv.fx;

import javafx.scene.control.Dialog;

import java.io.File;

/**
 * @author Morten Laukvik
 */
class LoadingTask extends Dialog{

    private final Main main;
    private final File file;

    public LoadingTask(Main main, File file) {
        this.main = main;
        this.file = file;
    }

}
