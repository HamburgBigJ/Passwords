package net.kyori.adventure.title;

import net.kyori.adventure.text.Component;

public record Title(Component title, Component subtitle) {
    public static Title title(Component title, Component subtitle) {
        return new Title(title, subtitle);
    }
}
