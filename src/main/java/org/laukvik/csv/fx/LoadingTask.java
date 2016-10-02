package org.laukvik.csv.fx;

import javafx.scene.control.Dialog;

import java.io.File;

/**
 * @author Morten Laukvik
 */
public class LoadingTask extends Dialog{

    Main main;
    File file;

    public LoadingTask(Main main, File file) {
        this.main = main;
        this.file = file;
    }

}
