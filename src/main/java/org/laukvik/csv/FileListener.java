package org.laukvik.csv;

import java.io.File;

/**
 *
 *
 * @author Morten Laukvik
 */
public interface FileListener {

    void beginRead( final File file );
    void finishRead( final File file );

    void readBytes( final long count, final long total );

    void beginWrite( final File file );
    void finishWrite( final File file );

}
