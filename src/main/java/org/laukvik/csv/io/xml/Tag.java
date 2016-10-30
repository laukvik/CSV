package org.laukvik.csv.io.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Morten Laukvik
 */
public class Tag {

    private static final String[] SINGLE_TAGS = {"IMG", "BR", "INPUT"};
    private final String name;
    private final List<Tag> children;
    private final List<Attribute> attributeList;
    private Tag parent;
    private String text;

    public Tag(String name) {
        this.name = name;
        this.attributeList = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public String getText() {
        return text;
    }

    public Tag getParent() {
        return parent;
    }

    public Tag addTag(String name) {
        return addTag(new Tag(name));
    }

    public Tag addTag(Tag tag) {
        tag.parent = this;
        children.add(tag);
        return tag;
    }

    public List<Attribute> getAttributes() {
        return attributeList;
    }

    public Attribute addAttribute(final String name) {
        return addAttribute(new Attribute(name));
    }

    public Attribute addAttribute(final Attribute attribute) {
        attributeList.add(attribute);
        return attribute;
    }

    public final boolean isText() {
        return text != null;
    }

    public final void setText(final String text) {
        this.text = text;
    }

    public final boolean isSingle() {
        for (int x = 0; x < SINGLE_TAGS.length; x++) {
            if (SINGLE_TAGS[x].equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final String toString() {
        return name;
    }

    public final String toHtml() {
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
            b.append("---/>");
            return b.toString();

        } else {
            if (parent != null) {
                b.append("<").append(name);
                for (Attribute a : attributeList) {
                    b.append(" ").append(a.toHtml());
                }
                b.append(">");
            }

            if (text == null) {
                for (Tag t : children) {
                    b.append(t.toHtml());
                }
            } else {
                b.append(text);
            }

            if (parent != null) {
                b.append("\n</").append(name).append(">");
            }
            return b.toString();
        }
    }
}

