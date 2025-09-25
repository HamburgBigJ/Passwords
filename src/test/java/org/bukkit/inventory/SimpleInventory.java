package org.bukkit.inventory;

import net.kyori.adventure.text.Component;

import java.util.List;

public class SimpleInventory implements Inventory {
    private final int size;
    private final Component title;
    private final List<ItemStack> contents;

    public SimpleInventory(int size, Component title, List<ItemStack> contents) {
        this.size = size;
        this.title = title;
        this.contents = contents;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public ItemStack getItem(int slot) {
        return contents.get(slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        contents.set(slot, itemStack);
    }

    public Component getTitle() {
        return title;
    }
}
