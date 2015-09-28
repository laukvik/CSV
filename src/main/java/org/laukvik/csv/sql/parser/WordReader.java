package org.laukvik.csv.sql.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordReader extends Reader {

    public WordReader() {
        super();
    }

    public String consume(String sql) throws SyntaxException {
        if (sql.startsWith("\"")) {
            throw new SyntaxException("Cant start with quotes");
        }
        Pattern p = Pattern.compile("^([\\w]*)");
        Matcher m = p.matcher(sql);

        if (m.find()) {
            String word = m.group();
            sql = sql.substring(m.end());
//			log( "Found word '" + word + "'" );
            addResults(word);
        } else {
            if (isRequired()) {
                throw new SyntaxException("Could not find a word");
            }
        }

        return sql;
    }

    public String getPurpose() {
        return "Consumes on word";
    }

}
