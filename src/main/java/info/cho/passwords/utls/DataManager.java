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

    public void savePlayerConfig(UUID playerUUID, FileConfiguration config) {
        File playerFile = new File(playerDataFolder, playerUUID.toString() + ".yml");
        try {
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerValue(Player player, String path, Object value) {
        FileConfiguration config = getPlayerConfig(player.getUniqueId());
        config.set(path, value);
        savePlayerConfig(player.getUniqueId(), config);
    }

    public Object getPlayerValue(Player player, String path) {
        FileConfiguration config = getPlayerConfig(player.getUniqueId());
        return config.get(path);
    }

    public void addValue(Player player, String variableName, Object defaultValue) {
        FileConfiguration config = getPlayerConfig(player.getUniqueId());

        if (!config.contains(variableName)) {
            config.set(variableName, defaultValue);
            savePlayerConfig(player.getUniqueId(), config);
        }
    }

    public void addListValue(Player player, String variableName, Object value) {
        FileConfiguration config = getPlayerConfig(player.getUniqueId());

        if (!config.contains(variableName)) {
            config.set(variableName, new ArrayList<>());
        }

        List<Object> list = (List<Object>) config.getList(variableName, new ArrayList<>());
        // Create a mutable copy if the list is immutable
        if (!(list instanceof ArrayList)) {
            list = new ArrayList<>(list);
        }

        if (!list.contains(value)) {
            list.add(value);
            config.set(variableName, list);
            savePlayerConfig(player.getUniqueId(), config);
        }
    }

    public void setListValue(Player player, String variableName, List<Object> list) {
        FileConfiguration config = getPlayerConfig(player.getUniqueId());

        if (!config.contains(variableName)) {
            config.set(variableName, new ArrayList<>());
        }

        config.set(variableName, list);
        savePlayerConfig(player.getUniqueId(), config);
    }

    public List<Object> getListValue(Player player, String variableName) {
        FileConfiguration config = getPlayerConfig(player.getUniqueId());
        return (List<Object>) config.getList(variableName, new ArrayList<>());
    }

    public boolean contains(Player player, String path) {
        FileConfiguration config = getPlayerConfig(player.getUniqueId());
        return config.contains(path);
    }

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
