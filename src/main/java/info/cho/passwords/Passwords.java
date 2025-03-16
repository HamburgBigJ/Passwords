package info.cho.passwords;

import info.cho.passwords.config.Config;
import info.cho.passwords.util.DataManager;
import io.fairyproject.FairyLaunch;
import io.fairyproject.log.Log;
import io.fairyproject.plugin.Plugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@FairyLaunch
public class Passwords extends Plugin {

    @Getter public static Passwords instance;
    @Getter public static Config config;
    @Getter public static DataManager dataManager;

    @Override
    public void onInitial() {
        instance = this;
        dataManager = new DataManager(getDataFolder().toFile());
        config = new Config();
    }

    @Override
    public void onPluginEnable() {
        Log.info("Plugin Enabled.");
    }

    public Inventory getInventory() {
        return Bukkit.createInventory(null, InventoryType.DISPENSER, config.getGuiName());
    }

    public Inventory getFirstJoinInventory() {
        return Bukkit.createInventory(null, InventoryType.DISPENSER, config.getSetPasswordName());
    }
}