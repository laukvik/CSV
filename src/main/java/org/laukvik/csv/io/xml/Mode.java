package org.laukvik.csv.io.xml;

/**
 * Indicates the state of parsing.
 */
public enum Mode {
    /**
     * Not parsed anything yet.
     */
    EMPTY,
    /**
     * In text mode.
     */
    TEXT,
    /**
     * The name of the tag.
     */
    TAG,
    /**
     * Inside a tag.
     */
    BODY,
    /**
     * Inside attribute name.
     */
    ATTR,
    /**
     * Is equal sign.
     */
    EQ,
    /**
     * The quote start.
     */
    QUOTE_OPEN,
    /**
     * The value of the attribute.
     */
    VALUE,
    /**
     * The quote end.
     */
    QUOTE_STOP,
    /**
     * The close symbol.
     */
    CLOSE
}
