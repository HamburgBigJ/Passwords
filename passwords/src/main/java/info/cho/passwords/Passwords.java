package info.cho.passwords;

import info.cho.passwords.customGui.CustomGui;
import info.cho.passwords.customGui.CustomGuiHandler;
import info.cho.passwords.placeholder.PlayerPlaceholder;
import info.cho.passwords.player.PasswordPlayerMode;
import info.cho.passwords.server.PasswordNoneMode;
import info.cho.passwords.server.PasswordPatternMode;
import info.cho.passwords.server.PasswordServerMode;
import info.cho.passwords.utls.PLog;
import info.cho.passwords.utls.Placeholders;
import info.cho.passwords.utls.PlayerInventorySave;
import info.cho.passwordsApi.password.PasswordConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Passwords extends JavaPlugin {

    public static Passwords instance;
    public static CustomGui customGui;
    public static CustomGuiHandler customGuiHandler;
    public static Placeholders placeholders;
    public static String version = "2.6.2";

    @Override
    public void onLoad() {
        instance = this;
        customGui = new CustomGui();
        placeholders = new Placeholders();
        saveDefaultConfig();

    }

    @Override
    public void onEnable() {
        customGuiHandler = new CustomGuiHandler();


        versionCheck();

        // PasswordGui registration
        customGui.registerGui("server", PasswordServerMode.class);
        customGui.registerGui("player", PasswordPlayerMode.class);
        customGui.registerGui("none", PasswordNoneMode.class);
        customGui.registerGui("pattern", PasswordPatternMode.class);

        // Placeholders
        placeholders.registerPlaceholder(PlayerPlaceholder.class);

        if (PasswordConfig.useAutoSave()) {
            PlayerInventorySave.savePlayerInventory();
        }

    }

    @Override
    public void onDisable() {
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

}
