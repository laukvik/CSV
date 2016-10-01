package org.laukvik.csv.fx;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.laukvik.csv.columns.Column;

/**
 * @author Morten Laukvik
 */
public class ObservableColumn {

    private SimpleBooleanProperty selected;
    private SimpleStringProperty name;
    private Column column;

    public ObservableColumn(final boolean selected, final Column column) {
        this.selected = new SimpleBooleanProperty(selected);
        this.name = new SimpleStringProperty(column.getName());
        this.column = column;
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected.set(selected);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(final String name) {
        this.name.set(name);
        column.setName(name);
    }
}
