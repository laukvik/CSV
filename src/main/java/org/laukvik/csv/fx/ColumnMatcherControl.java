package org.laukvik.csv.fx;

import javafx.scene.control.Control;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.query.RowMatcher;
import java.util.List;

/**
 *
 *
 */
public abstract class ColumnMatcherControl extends TabPane {

    final Column column;

    public ColumnMatcherControl(final Column column){
        super();
        this.column = column;
        getTabs().add( new Tab());
    }

    public abstract List<RowMatcher> getMatchers();

}
