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
import info.cho.passwords.server.PasswordServerMode;
import info.cho.passwords.utls.PLog;
import info.cho.passwordsApi.password.PasswordConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Passwords extends JavaPlugin {

    public static Passwords instance;
    public static CustomGui customGui;

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

        customGui.registerGui("server", PasswordServerMode.class);
        customGui.registerGui("player", PasswordPlayerMode.class);


    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        PLog.debug("Disabling passwords plugin");
    }


    private void versionCheck() {
        String version = "2.4";
        if(!Objects.equals(version, getConfig().getString("version"))) {
            PLog.warning("!!!!!!!!-----------------------------------------------------------------------------------!!!!!!!!");
            PLog.warning("Your version is outdated! Please delete the config.yml and restart the server to get the latest version!");
            PLog.warning("Your version: " + version);
            PLog.warning("Config version: " + PasswordConfig.getVersion());
            PLog.warning("!!!!!!!!-----------------------------------------------------------------------------------!!!!!!!!");
        } else {
            PLog.info("Your version is up to date!");

            PLog.debug("!!!!!!!!-----------------------------------------------------------------------------------!!!!!!!!");
            PLog.debug("Your version: " + version);
            PLog.debug("Config version: " + PasswordConfig.getVersion());
            PLog.debug("!!!!!!!!-----------------------------------------------------------------------------------!!!!!!!!");

        }
    }

}
