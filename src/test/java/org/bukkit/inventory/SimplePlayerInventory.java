package org.bukkit.inventory;

import net.kyori.adventure.text.Component;

import java.util.List;

public class SimplePlayerInventory extends SimpleInventory implements PlayerInventory {
    public SimplePlayerInventory(int size, Component title, List<ItemStack> contents) {
        super(size, title, contents);
    }
}
