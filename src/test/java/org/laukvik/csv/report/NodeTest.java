package org.laukvik.csv.report;

import org.junit.Test;
import org.laukvik.csv.columns.StringColumn;

import java.io.IOException;

/**
 *
 *
 *  Danmark København
 *  Norge   Bergen
 *  Norge   Oslo
 *  Sverige Arland
 *  Sverige Stockholm
 */
public class NodeTest {

    @Test
    public void test() throws IOException {
        StringColumn land = new StringColumn("land");
        StringColumn by = new StringColumn("by");
        StringColumn verdensdel = new StringColumn("verdensdel");
        Node root = new Node();
        root.add("Norge",  land).add("Oslo",by).add("Europa", verdensdel);
        root.add("Sverige",land).add("Arlanda",by);
        root.add("Norge",  land).add("Bergen",by);
        root.add("Danmark",land).add("København",by);
        root.add("Sverige",land).add("Stockholm",by).add("Europa", verdensdel);
//        root.getMap().forEach( (k,v) -> System.out.println( k + ": " + v.getCount()));
    }

}