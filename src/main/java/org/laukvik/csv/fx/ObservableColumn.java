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
public final class ObservableColumn {

    /**
     * The property for visible
     */
    private final SimpleBooleanProperty visible;
    /** The property for name */
    private final SimpleStringProperty name;
    /** The Column its for. */
    private final Column column;
    /** The Main instance. */
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

    /**
     * Returns the visibleProperty
     * @return the visibleProperty
     */
    public SimpleBooleanProperty visibleProperty() {
        return visible;
    }

    /**
     * Returns the nameProperty
     * @return the nameProperty
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     * Returns the name
     * @return the name
     */
    public String getName() {
        return column.getName();
    }

    /**
     * Sets the name.
     *
     * @param name the name
     */
    public void setName(final String name) {
        this.name.setValue(name);
        column.setName(name);
    }

}
