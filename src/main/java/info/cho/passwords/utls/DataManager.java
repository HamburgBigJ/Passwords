package info.cho.passwords.utls;

import info.cho.passwords.Passwords;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataManager {

    private final File playerDataFolder;
    private final Map<UUID, FileConfiguration> configCache = new HashMap<>();
    private FileConfiguration publicVarsConfig;

    public DataManager() {
        File pluginFolder = Passwords.instance.getDataFolder();
        // Create a folder for player files if it doesn't exist
        playerDataFolder = new File(pluginFolder, "data");

        File playerDataFolderOld = new File(pluginFolder, "playerdata");

        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
            if (playerDataFolderOld.isDirectory()) {
                for (File file : Objects.requireNonNull(playerDataFolderOld.listFiles())) {
                    File newFileLocation = new File(playerDataFolder, file.getName());
                    if (!file.renameTo(newFileLocation)) {
                        System.err.println("File " + file.getName() + " could not be moved to the new location.");
                    }
                }
            }


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

    // Retrieves all UUIDs from player configuration files
    public List<UUID> getAllPlayerUUIDs() {
        List<UUID> uuids = new ArrayList<>();

        if (playerDataFolder.exists() && playerDataFolder.isDirectory()) {
            File[] files = playerDataFolder.listFiles((dir, name) -> name.endsWith(".yml"));
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    try {
                        UUID uuid = UUID.fromString(fileName.replace(".yml", ""));
                        uuids.add(uuid);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid UUID in file name: " + fileName);
                    }
                }
            }
        }

        return uuids;
    }
}
