package info.cho.passwords;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import info.cho.passwords.commads.LogoutPlayerCommand;
import info.cho.passwords.commads.SetPasswordCommand;
import info.cho.passwords.commads.SetPlayerPasswordCommand;
import info.cho.passwords.customGui.CustomGui;
import info.cho.passwords.customGui.CustomGuiHandler;
import info.cho.passwords.hook.DiscordHook;
import info.cho.passwords.player.PasswordPlayerMode;
import info.cho.passwords.server.PasswordNoneMode;
import info.cho.passwords.server.PasswordPatternMode;
import info.cho.passwords.server.PasswordServerMode;
import info.cho.passwords.utls.DataManager;
import info.cho.passwords.utls.PLog;
import info.cho.passwordsApi.password.PasswordConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Passwords extends JavaPlugin {

    public static Passwords instance;
    public static CustomGui customGui;
    public static String version = "2.6";

    @Override
    public void onLoad() {
        instance = this;
        customGui = new CustomGui();
        saveDefaultConfig();
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(PasswordConfig.isDebugMode()));

    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        CustomGuiHandler customGuiHandler = new CustomGuiHandler(customGui);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PLog.info("Found PlaceholderAPI! Registering placeholders...");
        } else {
            PLog.warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (PasswordConfig.isUseDiscordLogin()) {
            DiscordHook discordHook = new DiscordHook();
        }


        if (PasswordConfig.isEnabled()) {
            PLog.info("Passwords plugin is enabled!");
        } else {
            PLog.warning("Passwords plugin is disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        versionCheck();

        LogoutPlayerCommand logoutPlayerCommand = new LogoutPlayerCommand();
        SetPlayerPasswordCommand setPlayerPasswordCommand = new SetPlayerPasswordCommand();
        SetPasswordCommand setPasswordCommand = new SetPasswordCommand();

        // PasswordGui registration
        customGui.registerGui("server", PasswordServerMode.class);
        customGui.registerGui("player", PasswordPlayerMode.class);
        customGui.registerGui("none", PasswordNoneMode.class);
        customGui.registerGui("pattern", PasswordPatternMode.class);

        if (PasswordConfig.useAutoSave()) {
            savePlayerInventory();
        }

    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        PLog.debug("Disabling passwords plugin");
    }


    private void versionCheck() {
        if(!Objects.equals(version, getConfig().getString("version"))) {
            PLog.warning("!!!!!!!!-----------------------------------------------------------------------------------!!!!!!!!");
            PLog.warning("Your version is outdated! Please delete the config.yml and restart the server to get the latest version!");
            PLog.warning("DO NOT DELETE THE Data FOLDER OR ALL INVENTORIES WILL BE LOST!");
            PLog.warning("Your version: " + version);
            PLog.warning("Config version: " + PasswordConfig.getVersion());
            PLog.warning("!!!!!!!!-----------------------------------------------------------------------------------!!!!!!!!");
        } else {
            PLog.info("Your version is up to date!");

            PLog.debug("!!!!!!!!------------------------ This is not an Error -------------------------------------!!!!!!!!");
            PLog.debug("Your version: " + version);
            PLog.debug("Config version: " + PasswordConfig.getVersion());
            PLog.debug("!!!!!!!!-----------------------------------------------------------------------------------!!!!!!!!");

        }
    }

    private void savePlayerInventory() {
        PLog.debug("Saving player inventory initialization...");

        int intervalSeconds = PasswordConfig.getSavePlayerInventoryIntervall() * 60; // Convert minutes to seconds
        int intervalTicks = intervalSeconds * 20; // Seconds to ticks (20 ticks per second)
        PLog.debug("Saving player inventory interval: " + intervalSeconds + " seconds (" + intervalTicks + " ticks)");

        getServer().getScheduler().runTaskTimer(this, () -> {
            PLog.debug("Saving player inventory...");
            for (Player player : Bukkit.getOnlinePlayers()) {
                DataManager dataManager = new DataManager();

                if (Objects.equals(dataManager.getPlayerValue(player, "isLogin").toString(), "false")) {
                    return;
                }

                Inventory playerInventory = player.getInventory();
                dataManager.setPlayerValue(player, "playerInventory", new ArrayList<>());

                for (int i = 0; i < playerInventory.getSize(); i++) {
                    if (playerInventory.getItem(i) != null) {
                        ItemStack currentItem = playerInventory.getItem(i);
                        List<Object> playerInventoryList = dataManager.getListValue(player, "playerInventory");
                        playerInventoryList.add(currentItem);
                        dataManager.setPlayerValue(player, "playerInventory", playerInventoryList);
                    } else {
                        ItemStack air = new ItemStack(Material.AIR);
                        List<Object> playerInventoryList = dataManager.getListValue(player, "playerInventory");
                        playerInventoryList.add(air);
                        dataManager.setPlayerValue(player, "playerInventory", playerInventoryList);
                    }
                }
            }
        }, intervalTicks, intervalTicks);

    }


}
