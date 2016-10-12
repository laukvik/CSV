package org.laukvik.csv.fx;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.laukvik.csv.columns.Column;

/**
 * @author Morten Laukvik
 */
public class ObservableColumn{

    private SimpleBooleanProperty visible;
    private SimpleStringProperty name;
    private Column column;
    private Main main;

    public ObservableColumn(final Column column) {
        visible = new SimpleBooleanProperty(column.isVisible());
        name = new SimpleStringProperty(column.getName());
        this.column = column;
        visible.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) {
                column.setVisible(newValue);
            }
        });
    }

    public void setName(String name) {
        this.name.setValue(name);
        column.setName(name);
    }

    public SimpleBooleanProperty visibleProperty() {
        return visible;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return column.getName();
    }

}
