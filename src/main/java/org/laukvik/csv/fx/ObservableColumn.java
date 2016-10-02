package org.laukvik.csv.fx;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.laukvik.csv.columns.Column;

/**
 * @author Morten Laukvik
 */
public class ObservableColumn implements ChangeListener<Boolean> {

    private SimpleBooleanProperty visible;
    private SimpleStringProperty name;
    private Column column;

    public ObservableColumn(final boolean selected, final Column column) {
        this.visible = new SimpleBooleanProperty(selected);
        this.name = new SimpleStringProperty(column.getName());
        this.column = column;
        visible.addListener(this);
    }

    public SimpleBooleanProperty visibleProperty() {
        return visible;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public boolean getVisible() {
        return visible.getValue();
    }

    public void setVisible(final boolean visible) {
        this.visible.set(visible);
        this.column.setVisible(visible);
    }

    public String getName() {
        return column.getName();
    }

    public void setName(final String name) {
        this.name.setValue(name);
        column.setName(name);
    }

    @Override
    public void changed(final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean visible) {
        this.column.setVisible(visible);
    }
}
