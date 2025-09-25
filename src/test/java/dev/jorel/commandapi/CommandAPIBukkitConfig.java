package dev.jorel.commandapi;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandAPIBukkitConfig extends CommandAPIConfig {
    private final JavaPlugin plugin;
    private boolean verbose;

    public CommandAPIBukkitConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPIConfig verboseOutput(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public JavaPlugin plugin() {
        return plugin;
    }

    public boolean verboseOutput() {
        return verbose;
    }
}
