package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Reads the words in a text file and creates a distribution table of them.
 */
public class WordCountReader implements Readable {

    /**
     * Map containing words and count.
     */
    private final Map<String, Integer> map;
    /** The MetaData. */
    private final MetaData metaData;
    /** The word column. */
    private final StringColumn wordColumn;
    /** The count column. */
    private final StringColumn countColumn;
    /** The current row. */
    private Row row;
    /** The row counter. */
    private int index;
    /** The list of words. */
    private List<String> list;

    /**
     * Creates a new instance.
     */
    public WordCountReader() {
        map = new TreeMap<>();
        metaData = new MetaData();
        wordColumn = new StringColumn("Word");
        countColumn = new StringColumn("Count");
        metaData.addColumn(wordColumn);
        metaData.addColumn(countColumn);
    }

    /**
     * Reads the file.
     *
     * @param file the file
     * @throws FileNotFoundException when the file cant be found
     */
    public final void readFile(final File file) throws FileNotFoundException {
        parse(file);
    }

    /**
     * Returns a trimmed and lowercase version of the word which is null pointer safe.
     *
     * @param word the word
     * @return the trimmed version
     */
    private String cleaned(final String word) {
        if (word == null) {
            return null;
        } else {
            return word.trim().toLowerCase().replaceAll("\\d", "").replaceAll("\\W", "");
        }
    }

    /**
     * Parses the file.
     *
     * @param file the file
     * @throws FileNotFoundException when the file cant be found
     */
    private void parse(final File file) throws FileNotFoundException {
        final FileReader r = new FileReader(file);
        try (Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                String str = s.nextLine();
                StringTokenizer t = new StringTokenizer(str);
                while (t.hasMoreTokens()) {
                    String word = cleaned(t.nextToken());
                    if (word != null && !word.isEmpty()) {
                        if (map.containsKey(word)) {
                            Integer count = map.get(word) + 1;
                            map.put(word, count);
                        } else {
                            map.put(word, 1);
                        }
                    }
                }
            }
        }
        list = new ArrayList<>();
        list.addAll(map.keySet());
        Collections.sort(list);
        index = 0;
    }

    /**
     * Returns the MetaData.
     *
     * @return the MetaData
     */
    public final MetaData getMetaData() {
        return metaData;
    }

    /**
     * Return the current row.
     *
     * @return the row
     */
    public final Row getRow() {
        return row;
    }

    /**
     * Returns true if more rows are available.
     *
     * @return true if more rows
     */
    public final boolean hasNext() {
        return index < map.size();
    }

    /**
     * Returns the next row.
     *
     * @return the next row
     */
    public final Row next() {
        row = new Row();
        String key = list.get(index);
        row.setString(wordColumn, list.get(index)).setString(countColumn, map.get(key) + "");
        index++;
        return row;
    }
}
