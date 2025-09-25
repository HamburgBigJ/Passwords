package org.bukkit.inventory;

public enum InventoryType {
    DROPPER(9);

    private final int defaultSize;

    InventoryType(int defaultSize) {
        this.defaultSize = defaultSize;
    }

    public int getDefaultSize() {
        return defaultSize;
    }
}
