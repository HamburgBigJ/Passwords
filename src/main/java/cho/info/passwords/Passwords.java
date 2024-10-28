package cho.info.passwords;

import cho.info.passwords.player.PasswordPlayer;
import cho.info.passwords.server.PasswordServer;
import cho.info.passwords.universal.PasswordsUniversal;
import cho.info.passwords.utls.ConfigManager;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public final class Passwords extends JavaPlugin {

    public ConfigManager configManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        ConfigManager configManager = new ConfigManager(getDataFolder());
        PasswordServer passwordServer = new PasswordServer(this, configManager);
        PasswordsUniversal passwordsUniversal = new PasswordsUniversal(this, configManager);
        PasswordPlayer passwordPlayer = new PasswordPlayer(configManager, this);

        getLogger().info("Passwords enabled!");
        saveDefaultConfig();

        if (!getConfig().getBoolean("enable")) {
            getLogger().info("Passwords disabled!");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            afterCheck(passwordServer, passwordsUniversal, passwordPlayer);
        }


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }

    public void afterCheck(PasswordServer passwordServer, PasswordsUniversal passwordsUniversal, PasswordPlayer passwordPlayer) {
        // Plugin enable
        // Register Server Listeners
        passwordServer.listeners();
        // Register Universal Listeners
        passwordsUniversal.listeners();
        // Register Player Listeners
        passwordPlayer.listeners();

    }
}
