package org.bukkit.plugin.java;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class JavaPlugin implements org.bukkit.plugin.Plugin {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final File dataFolder;
    private final FileConfiguration configuration = new FileConfiguration();
    private boolean enabled;

    public JavaPlugin() {
        this.dataFolder = new File("build/test-data/" + getClass().getSimpleName());
        this.dataFolder.mkdirs();
    }

    public void onLoad() {
    }

    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    public Logger getLogger() {
        return logger;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    public FileConfiguration getConfig() {
        return configuration;
    }

    public void saveDefaultConfig() {
        saveConfig();
    }

    public void saveConfig() {
        try {
            configuration.save(new File(dataFolder, "config.dat"));
        } catch (IOException ignored) {
        }
    }

    public void reloadConfig() {
        try {
            configuration.load(new File(dataFolder, "config.dat"));
        } catch (IOException ignored) {
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
