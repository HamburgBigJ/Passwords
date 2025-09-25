package org.bukkit.inventory.meta;

import net.kyori.adventure.text.Component;

public class ItemMeta implements java.io.Serializable {
    private Component displayName;

    public void displayName(Component component) {
        this.displayName = component;
    }

    public Component displayName() {
        return displayName;
    }
}
