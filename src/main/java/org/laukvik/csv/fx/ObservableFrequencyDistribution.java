package org.laukvik.csv.fx;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

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
     * @param selected
     * @param value
     * @param count
     */
    public ObservableFrequencyDistribution(boolean selected, String value, int count) {
        this.selected = new SimpleBooleanProperty(selected);
        this.value = new SimpleStringProperty(value);
        this.count = new SimpleIntegerProperty(count);
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
