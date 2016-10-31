package org.laukvik.csv.fx;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.laukvik.csv.columns.Column;

/**
 * A JavaFX data model for FrequencyDistribution.
 *
 * @author Morten Laukvik
 */
@SuppressWarnings("WeakerAccess")
public final class ObservableFrequencyDistribution {

    private final SimpleBooleanProperty selected;
    private final SimpleStringProperty value;
    private final SimpleIntegerProperty count;

    /**
     * Builds a new instance with the specified values.
     *
     * @param selected whether the value is selected
     * @param value the value
     * @param count how many times its used
     * @param column the column
     * @param main the main
     */
    public ObservableFrequencyDistribution(final boolean selected, final String value, final int count, final Column column, final Main main) {
        this.selected = new SimpleBooleanProperty(selected);
        this.value = new SimpleStringProperty(value);
        this.count = new SimpleIntegerProperty(count);
        this.selected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) {
                if (main != null) {
                    if (newValue) {
                        main.handleSelected(column, value);
                    } else {
                        main.handleUnselected(column, value);
                    }
                }
            }
        });
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public int getCount() {
        return count.get();
    }

    public SimpleIntegerProperty countProperty() {
        return count;
    }
}
