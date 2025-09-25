package org.bukkit.plugin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public interface PluginManager {
    boolean isPluginEnabled(String name);
    void setPluginEnabled(String name, boolean enabled);
    void disablePlugin(Plugin plugin);
    void registerEvents(Listener listener, Plugin plugin);
}
