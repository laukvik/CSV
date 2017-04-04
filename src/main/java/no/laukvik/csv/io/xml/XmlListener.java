package no.laukvik.csv.io.xml;

/**
 * Listener for when reading XML.
 */
public interface XmlListener {
    /**
     * Found the tag.
     *
     * @param tag the tag
     */
    void foundTag(Tag tag);

    /**
     * Found the attribute.
     *
     * @param attribute the attribute
     */
    void foundAttribute(Attribute attribute);
}
