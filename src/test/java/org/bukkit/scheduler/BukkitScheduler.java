package org.bukkit.scheduler;

import org.bukkit.plugin.Plugin;

public interface BukkitScheduler {
    BukkitTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period);
    void performTicks(long ticks);
}
