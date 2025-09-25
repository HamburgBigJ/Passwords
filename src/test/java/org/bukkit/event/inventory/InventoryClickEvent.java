package org.bukkit.event.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickEvent {
    private final Player whoClicked;
    private final Inventory inventory;
    private final int slot;
    private final ItemStack currentItem;
    private boolean cancelled;

    public InventoryClickEvent(Player whoClicked, Inventory inventory, int slot, ItemStack currentItem) {
        this.whoClicked = whoClicked;
        this.inventory = inventory;
        this.slot = slot;
        this.currentItem = currentItem;
    }

    public Player getWhoClicked() {
        return whoClicked;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getCurrentItem() {
        return currentItem;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
