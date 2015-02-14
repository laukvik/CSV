package org.laukvik.csv.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class TextDriver implements java.sql.Driver {

    private static final String URL_PREFIX = "jdbc:TextDriver";

    static {
        try {
            DriverManager.registerDriver(new TextDriver());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean acceptsURL(String url) throws SQLException {
        if (url.startsWith(URL_PREFIX)) {
            String folder = url.substring(url.lastIndexOf(":") + 1);
            File home = new File(folder);
            if (home.exists() && home.isDirectory()) {
                return true;
            } else {
                throw new SQLException("Folder does not exist: " + folder);
            }
        } else {
            return false;
        }
    }

    public Connection connect(String url, Properties info) throws SQLException {
        if (acceptsURL(url)) {
            return new TextConnection(url, info);
        }
        return null;
    }

    public int getMajorVersion() {
        return 1;
    }

    public int getMinorVersion() {
        return 0;
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    public boolean jdbcCompliant() {
        return false;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
