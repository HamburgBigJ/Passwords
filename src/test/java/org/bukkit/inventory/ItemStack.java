package org.bukkit.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStack implements java.io.Serializable {
    private final Material type;
    private int amount = 1;
    private ItemMeta itemMeta;

    public ItemStack(Material type) {
        this.type = type;
    }

    public Material getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    public void setItemMeta(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
    }
}
