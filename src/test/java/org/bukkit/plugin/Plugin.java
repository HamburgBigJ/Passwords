package org.bukkit.plugin;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public interface Plugin {
    Server getServer();
    boolean isEnabled();
    void onDisable();
    default JavaPlugin asJavaPlugin() {
        return (JavaPlugin) this;
    }
}
