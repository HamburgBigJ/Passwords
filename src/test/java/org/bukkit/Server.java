package org.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Collection;

public interface Server {
    PluginManager getPluginManager();
    BukkitScheduler getScheduler();
    Collection<Player> getOnlinePlayers();
}
