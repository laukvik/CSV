package org.laukvik.csv.fx;

import javafx.stage.FileChooser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Morten Laukvik
 */
public class FileChooserExtensions {

    public final static FileChooser.ExtensionFilter [] SUPPORTED = {
            buildCSV(), buildResourceBundle(), buildTextFile(), buildJsonFile(), buildHtmlFile(), buildXmlFile()
    };


    public static FileChooser.ExtensionFilter buildCSV() {
        List<String> list = new ArrayList<>();
        list.add(".csv");
        return new FileChooser.ExtensionFilter("Comma separated files", "*.csv");
    }

    public static FileChooser.ExtensionFilter buildResourceBundle() {
        return new FileChooser.ExtensionFilter("Resource bundles", "*.properties");
    }

    private static FileChooser.ExtensionFilter buildTextFile() {
        return new FileChooser.ExtensionFilter("Text files", "*.txt");
    }

    public static FileChooser.ExtensionFilter buildJsonFile() {
        return new FileChooser.ExtensionFilter("JSON files", "*.json");
    }

    public static FileChooser.ExtensionFilter buildHtmlFile() {
        return new FileChooser.ExtensionFilter("HTML files", "*.html");
    }

    public static FileChooser.ExtensionFilter buildXmlFile() {
        return new FileChooser.ExtensionFilter("XML files", "*.xml");
    }

}
