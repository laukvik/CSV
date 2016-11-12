package org.laukvik.csv.io;

import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Reads the words in a text file and creates a distribution table of them.
 */
public final class WordCountReader implements DatasetFileReader {

    /** The word column. */
    private final StringColumn wordColumn;
    /** The count column. */
    private final StringColumn countColumn;
    /** The list of words. */
    private List<String> list;

    /**
     * Creates a new instance.
     */
    public WordCountReader() {
        wordColumn = new StringColumn("Word");
        countColumn = new StringColumn("Count");
    }

    /**
     * Returns a trimmed and lowercase version of the word which is null pointer safe.
     *
     * @param word the word
     * @return the trimmed version
     */
    public static String cleaned(final String word) {
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
    public static Map<String, Integer> parse(final File file) throws FileNotFoundException {
        Map<String, Integer> map = new HashMap<>();
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
        return map;
    }

    /**
     * Reads the file.
     *
     * @param file the file
     * @param csv the csv
     * @throws FileNotFoundException when the file cant be found
     */
    public void readFile(final File file, final CSV csv) throws FileNotFoundException {
        csv.addColumn(wordColumn);
        csv.addColumn(countColumn);
        parse(file);
        Map<String, Integer> map = parse(file);
        list = new ArrayList<>();
        list.addAll(map.keySet());
        Collections.sort(list);
        int index = 0;
        for (String key : list) {
            csv.addRow().setString(wordColumn, list.get(index)).setString(countColumn, map.get(key) + "");
            index++;
        }
    }

}
