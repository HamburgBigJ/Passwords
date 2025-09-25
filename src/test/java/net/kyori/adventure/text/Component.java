package net.kyori.adventure.text;

import net.kyori.adventure.text.format.NamedTextColor;

public class Component implements java.io.Serializable {
    private final String content;
    private NamedTextColor color;

    private Component(String content) {
        this.content = content;
    }

    public static Component text(Object content) {
        return new Component(String.valueOf(content));
    }

    public static Component text(Object content, NamedTextColor color) {
        return text(content).color(color);
    }

    public Component color(NamedTextColor color) {
        this.color = color;
        return this;
    }

    public String content() {
        return content;
    }

    public NamedTextColor color() {
        return color;
    }
}
