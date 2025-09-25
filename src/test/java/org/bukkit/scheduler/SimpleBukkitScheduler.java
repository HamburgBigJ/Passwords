package org.bukkit.scheduler;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleBukkitScheduler implements BukkitScheduler {
    private static class ScheduledTask {
        private final Plugin plugin;
        private final Runnable runnable;
        private final long period;
        private long nextRunTick;

        private ScheduledTask(Plugin plugin, Runnable runnable, long delay, long period) {
            this.plugin = plugin;
            this.runnable = runnable;
            this.period = period;
            this.nextRunTick = delay;
        }
    }

    private long currentTick;
    private final List<ScheduledTask> tasks = new ArrayList<>();

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
        tasks.add(new ScheduledTask(plugin, runnable, delay, period));
        return new BukkitTask();
    }

    @Override
    public void performTicks(long ticks) {
        long targetTick = currentTick + ticks;
        while (currentTick < targetTick) {
            currentTick++;
            Iterator<ScheduledTask> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                ScheduledTask task = iterator.next();
                if (currentTick >= task.nextRunTick) {
                    task.runnable.run();
                    task.nextRunTick += task.period;
                    if (task.period <= 0) {
                        iterator.remove();
                    }
                }
            }
        }
    }
}
