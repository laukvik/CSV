package org.laukvik.csv.fx;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.laukvik.csv.columns.Column;

/**
 * Represents a column that is observable for JavaFX components.
 *
 * @author Morten Laukvik
 */
@SuppressWarnings("WeakerAccess")
public final class ObservableColumn{

    private final SimpleBooleanProperty visible;
    private final SimpleStringProperty name;
    private final Column column;
    private Main main;

    /**
     * Represents a column that is observable.
     *
     * @param column the column
     */
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

    public SimpleBooleanProperty visibleProperty() {
        return visible;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return column.getName();
    }

    public void setName(String name) {
        this.name.setValue(name);
        column.setName(name);
    }

}
