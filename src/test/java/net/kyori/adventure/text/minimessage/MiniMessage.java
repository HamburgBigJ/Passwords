package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.Component;

public final class MiniMessage {
    private static final MiniMessage INSTANCE = new MiniMessage();

    private MiniMessage() {
    }

    public static MiniMessage miniMessage() {
        return INSTANCE;
    }

    public Component deserialize(String input) {
        return Component.text(input);
    }
}
