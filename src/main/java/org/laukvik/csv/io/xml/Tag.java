package org.laukvik.csv.io.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains zero or more tags.
 */
public final class Tag {

    /**
     * All tags that doesn't have an ending tag.
     */
    private static final String[] SINGLE_TAGS = {"IMG", "BR", "INPUT"};
    /**
     * The name.
     */
    private final String name;
    /**
     * All children tags.
     */
    private final List<Tag> children;
    /**
     * All attributes.
     */
    private final List<Attribute> attributeList;
    /**
     * The parent tag.
     */
    private Tag parent;
    /**
     * The text value.
     */
    private String text;

    /**
     * Creates a new Tag with the name.
     *
     * @param tagName the tag name
     */
    public Tag(final String tagName) {
        this.name = tagName;
        this.attributeList = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    /**
     * Returns all its children.
     *
     * @return the children
     */
    public List<Tag> getChildren() {
        return children;
    }

    /**
     * Returns the name of the tag.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the text value.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the parent tag.
     *
     * @return the parent
     */
    public Tag getParent() {
        return parent;
    }

    /**
     * Adds a tag with the name.
     *
     * @param tagName the name of the tag
     * @return the new tag
     */
    public Tag addTag(final String tagName) {
        return addTag(new Tag(tagName));
    }

    /**
     * Adds the tag as a child.
     *
     * @param tag the tag
     * @return the tag
     */
    public Tag addTag(final Tag tag) {
        tag.parent = this;
        children.add(tag);
        return tag;
    }

    /**
     * Returns all attributes.
     *
     * @return all attributes
     */
    public List<Attribute> getAttributes() {
        return attributeList;
    }

    /**
     * Creates and adds an attribute.
     *
     * @param attributeName the name of the attribute
     * @return the added attribute
     */
    public Attribute addAttribute(final String attributeName) {
        return addAttribute(new Attribute(attributeName));
    }

    /**
     * Adds the attribute.
     *
     * @param attribute the attribute
     * @return the added attribute
     */
    public Attribute addAttribute(final Attribute attribute) {
        attributeList.add(attribute);
        return attribute;
    }

    /**
     * Returns true if the tag is a text node.
     *
     * @return true if text node
     */
    public boolean isText() {
        return text != null;
    }

    /**
     * Sets the text value.
     *
     * @param value the text value
     */
    public void setText(final String value) {
        this.text = value;
    }

    /**
     * Returns true if the tag is a single tag.
     *
     * @return true if the tag is a single tag
     */
    public boolean isSingle() {
        for (int x = 0; x < SINGLE_TAGS.length; x++) {
            if (SINGLE_TAGS[x].equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the tag and all its children as HTML.
     *
     * @return HTML
     */
    public String toHtml() {
        StringBuilder b = new StringBuilder();
        if (isText()) {
            b.append(text);
            return b.toString();

        } else if (isSingle()) {

            b.append("<").append(name);
            for (Attribute a : attributeList) {
                b.append(" ").append(a.toHtml());
            }

            if (text == null) {
                for (Tag t : children) {
                    b.append(t.toHtml());
                }
            } else {
                b.append(text);
            }
            b.append(">");
            return b.toString();

        } else {
            b.append("<").append(name);
            for (Attribute a : attributeList) {
                b.append(" ").append(a.toHtml());
            }
            b.append(">");

            if (text == null) {
                for (Tag t : children) {
                    b.append(t.toHtml());
                }
            } else {
                b.append(text);
            }

            b.append("</").append(name).append(">");
            return b.toString();
        }
    }

    /**
     * Returns an attribute by it's name.
     *
     * @param name the attribute name
     * @return the attribute
     */
    public Attribute getAttribute(final String name) {
        for (Attribute a : attributeList) {
            if (a.getName().equalsIgnoreCase(name)) {
                return a;
            }
        }
        return null;
    }
}

