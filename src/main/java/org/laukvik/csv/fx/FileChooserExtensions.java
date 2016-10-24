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


    public final static FileChooser.ExtensionFilter buildCSV(){
        List<String> list = new ArrayList<>();
        list.add(".csv");
        return new FileChooser.ExtensionFilter("Comma separated files", "*.csv");
    }

    public final static FileChooser.ExtensionFilter buildResourceBundle(){
        return new FileChooser.ExtensionFilter("Resource bundles", "*.properties");
    }

    public final static FileChooser.ExtensionFilter buildTextFile(){
        return new FileChooser.ExtensionFilter("Text files", "*.txt");
    }

    public final static FileChooser.ExtensionFilter buildJsonFile(){
        return new FileChooser.ExtensionFilter("JSON files", "*.json");
    }

    public final static FileChooser.ExtensionFilter buildHtmlFile(){
        return new FileChooser.ExtensionFilter("HTML files", "*.html");
    }

    public final static FileChooser.ExtensionFilter buildXmlFile(){
        return new FileChooser.ExtensionFilter("XML files", "*.xml");
    }

}
