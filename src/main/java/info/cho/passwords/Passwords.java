package info.cho.passwords;

import github.scarsz.discordsrv.DiscordSRV;
import info.cho.passwords.config.Config;
import info.cho.passwords.hooks.DiscordSrvHook;
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
    @Getter public static String pluginVersion = "2.2";

    @Override
    public void onInitial() {
        instance = this;
        dataManager = new DataManager(getDataFolder().toFile());

        DiscordSrvHook discordSrvHook = new DiscordSrvHook();

    }

    @Override
    public void onPluginEnable() {
        Log.info("Plugin Enabled.");

        config = new Config();

        if (!config.getConfigVersion().equals(pluginVersion)) {
            Log.info("Config version is not up to date! Please delete the config.yml file and restart the server.");
        }
    }

    @Override
    public void onPluginDisable() {

        if (Bukkit.getServer().getPluginManager().getPlugin("DiscordSRV") != null) {
            DiscordSRV.api.unsubscribe(this);
            Log.info("DiscordSRV features disabled!");
        }

    }

    public Inventory getInventory() {
        return Bukkit.createInventory(null, InventoryType.DISPENSER, config.getGuiName());
    }

    public Inventory getFirstJoinInventory() {
        return Bukkit.createInventory(null, InventoryType.DISPENSER, config.getSetPasswordName());
    }
}