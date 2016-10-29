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
 *
 *
 */
public class WordCountReader implements Readable{

    private final Map<String, Integer> map;
    private final MetaData metaData;
    private final StringColumn wordColumn;
    private final StringColumn countColumn;
    private Row row;
    private int index;
    private List<String> list;

    public WordCountReader() {
        map = new TreeMap<>();
        metaData = new MetaData();
        wordColumn = new StringColumn("Word");
        countColumn = new StringColumn("Count");
        metaData.addColumn(wordColumn);
        metaData.addColumn(countColumn);
    }

    public void readFile(File file) throws FileNotFoundException {
        parse(file);
    }

    private String cleaned(final String word){
        if (word == null) {
            return null;
        } else {
            return word.trim().toLowerCase().replaceAll( "\\d", "" ).replaceAll( "\\W", "" );
        }
    }

    private void parse(File file) throws FileNotFoundException {
        final FileReader r = new FileReader(file);
        try (Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                String str = s.nextLine();
                StringTokenizer t = new StringTokenizer(str);
                while (t.hasMoreTokens()) {
                    String word = cleaned( t.nextToken() );
                    if (word != null && !word.isEmpty()){
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

    @Override
    public MetaData getMetaData() {
        return metaData;
    }

    @Override
    public Row getRow() {
        return row;
    }

    @Override
    public boolean hasNext() {
        return index < map.size();
    }

    @Override
    public Row next() {
        row = new Row();
        String key = list.get(index);
        row.update(wordColumn, list.get(index) ).update(countColumn, map.get(key) + "");
        index++;
        return row;
    }
}
