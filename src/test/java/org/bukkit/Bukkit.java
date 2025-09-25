package org.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.SimpleInventory;
import org.bukkit.inventory.InventoryType;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class Bukkit {
    private static Server server;

    private Bukkit() {
    }

    public static void setServer(Server serverInstance) {
        server = serverInstance;
    }

    public static Server getServer() {
        return server;
    }

    public static PluginManager getPluginManager() {
        return server.getPluginManager();
    }

    public static Collection<Player> getOnlinePlayers() {
        return server.getOnlinePlayers();
    }

    public static Inventory createInventory(InventoryHolder holder, InventoryType type, net.kyori.adventure.text.Component title) {
        return new SimpleInventory(type.getDefaultSize(), title, new ArrayList<>(Collections.nCopies(type.getDefaultSize(), null)));
    }

    public static void callEvent(InventoryCloseEvent event) {
        // no-op in the simplified environment
    }
}
