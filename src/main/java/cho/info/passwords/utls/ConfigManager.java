package cho.info.passwords.utls;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConfigManager {

    private final File playerDataFolder;
    private final Map<UUID, FileConfiguration> configCache = new HashMap<>();
    private FileConfiguration publicVarsConfig;

    public ConfigManager(File pluginFolder) {
        // Create a folder for player files if it doesn't exist
        playerDataFolder = new File(pluginFolder, "playerdata");
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }


    }

    // Loads or retrieves the config file for the player based on UUID
    private FileConfiguration getPlayerConfig(UUID playerUUID) {
        File playerFile = new File(playerDataFolder, playerUUID.toString() + ".yml");
        FileConfiguration config = configCache.get(playerUUID);

        if (config == null || !playerFile.exists()) {
            if (!playerFile.exists()) {
                try {
                    playerFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            config = YamlConfiguration.loadConfiguration(playerFile);
            configCache.put(playerUUID, config);
        }

        return config;
    }

    // Saves the player's config file
    public void savePlayerConfig(UUID playerUUID, FileConfiguration config) {
        File playerFile = new File(playerDataFolder, playerUUID.toString() + ".yml");
        try {
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sets a value (e.g., FarmingXP) for the player
    public void setPlayerValue(Player player, String path, Object value) {
        FileConfiguration config = getPlayerConfig(player.getUniqueId());
        config.set(path, value);
        savePlayerConfig(player.getUniqueId(), config);
    }

    // Retrieves a value (e.g., FarmingXP) for the player
    public Object getPlayerValue(Player player, String path) {
        FileConfiguration config = getPlayerConfig(player.getUniqueId());
        return config.get(path);
    }

    // Adds a new variable with a default value
    public void addValue(Player player, String variableName, Object defaultValue) {
        FileConfiguration config = getPlayerConfig(player.getUniqueId());

        if (!config.contains(variableName)) {
            config.set(variableName, defaultValue);
            savePlayerConfig(player.getUniqueId(), config);
        }
    }

    // Checks if a variable exists
    public boolean contains(Player player, String path) {
        FileConfiguration config = getPlayerConfig(player.getUniqueId());
        return config.contains(path);
    }

}
