package org.laukvik.csv.columns;

/**
 * Specifies a value with an optional extra value.
 */
public final class Attribute {

    private String value;
    private String extra;

    /**
     * @param value
     */
    public Attribute(final String value) {
        this.value = value;
        int firstIndex = value.indexOf("[");
        if (firstIndex > -1) {
            int lastIndex = value.lastIndexOf("]");
            if (lastIndex > firstIndex) {
                String v = value.substring(firstIndex + 1, lastIndex);
                this.extra = v;
                this.value = value.substring(0, firstIndex);
            }
        }
    }

    public Attribute(final String value, final String extra) {
        this.value = value;
        this.extra = extra;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String toCompressed() {
        if (extra != null) {
            return value + "[" + extra + "]";
        } else {
            return value;
        }
    }
}
