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
import info.cho.passwords.utls.PLog;
import info.cho.passwordsApi.password.PasswordConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
