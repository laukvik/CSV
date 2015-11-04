package org.laukvik.csv.sql.parser;

import org.laukvik.csv.sql.Table;

public interface TableListener {

    public void found(Table table);
}
