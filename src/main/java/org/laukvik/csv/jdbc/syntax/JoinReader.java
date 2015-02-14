package org.laukvik.csv.jdbc.syntax;

import java.util.ArrayList;
import java.util.List;
import org.laukvik.csv.jdbc.Join;

public class JoinReader extends GroupReader {

    private List<JoinReaderListener> listeners;

    public JoinReader() {
        listeners = new ArrayList<>();
    }

    public void addJoinReaderListener(JoinReaderListener listener) {
        listeners.add(listener);
    }

    public void removeJoinReaderListener(JoinReaderListener listener) {
        listeners.remove(listener);
    }

    public void fireJoinFound(Join join) {
        for (JoinReaderListener l : listeners) {
            l.found(join);
        }
    }

}
