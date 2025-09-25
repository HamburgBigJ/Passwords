package org.bukkit.configuration.file;

import java.io.File;
import java.io.IOException;

public class YamlConfiguration extends FileConfiguration {
    public static YamlConfiguration loadConfiguration(File file) {
        YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException ignored) {
        }
        return configuration;
    }
}
