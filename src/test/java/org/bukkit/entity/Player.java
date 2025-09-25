package org.bukkit.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public interface Player {
    UUID getUniqueId();
    String getName();
    org.bukkit.inventory.PlayerInventory getInventory();
    void setInventory(org.bukkit.inventory.PlayerInventory inventory);
    void openInventory(Inventory inventory);
    void closeInventory();
    boolean isDead();
    Spigot spigot();
    void setInvulnerable(boolean invulnerable);
    void kick(Component message);
    void sendActionBar(Component component);
    void showTitle(Title title);
    void sendMessage(String message);
    void setGameMode(GameMode gameMode);
    PermissionAttachment addAttachment(JavaPlugin plugin, String permission, boolean value);

    interface Spigot {
        void respawn();
    }
}
