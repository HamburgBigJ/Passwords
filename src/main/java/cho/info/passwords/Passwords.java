package cho.info.passwords;

import cho.info.passwords.api.PasswordsApi;
import cho.info.passwords.player.PasswordPlayer;
import cho.info.passwords.player.commands.PlayerCommands;
import cho.info.passwords.publicCommands.PublicCommands;
import cho.info.passwords.server.PasswordServer;
import cho.info.passwords.utls.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Passwords extends JavaPlugin {

    public ConfigManager configManager;
    public String version = "0.1.3";

    public PasswordsApi passwordsApi;

    @Override
    public void onEnable() {
        // Plugin startup logic

        ConfigManager configManager = new ConfigManager(getDataFolder());
        PasswordServer passwordServer = new PasswordServer(this, configManager);
        PasswordPlayer passwordPlayer = new PasswordPlayer(configManager, this);
        PublicCommands publicCommands = new PublicCommands(this);
        PlayerCommands playerCommands = new PlayerCommands(this, configManager);

        passwordsApi = new PasswordsApi(this, configManager);

        getLogger().info("Passwords enabled!");
        saveDefaultConfig();

        if (!getConfig().getBoolean("enable")) {
            getLogger().info("Passwords disabled!");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            afterCheck(passwordServer, passwordPlayer);
        }

        // Mode commands
        if (getConfig().getString("settings.check-type").equals("player") || getConfig().getString("settings.check-type").equals("server")) {
            publicCommands.registerCommands();
            playerCommands.registerPlayerCommands();

        } else {
            getLogger().info(ChatColor.RED + "Unable to read Config.yml settings.check-type ");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }

        if (getConfig().getString("version") != version) {
            getLogger().info(ChatColor.GREEN + "Update Config!");
            File file = new File(getDataFolder(), "config.yml");
            file.delete();
            saveDefaultConfig();
        }
        // Fail save
        if (getConfig().getString("version") == null) {
            getLogger().info(ChatColor.GREEN + "Update Config!");
            File file = new File(getDataFolder(), "config.yml");
            file.delete();
            saveDefaultConfig();
        }

        if (getConfig().getBoolean("api.enable")) {
            getLogger().info("API enabled!");

        } else {
            getLogger().info("API disabled!");
        }

        getLogger().info("Your are currently running version: " + version);
        getLogger().info("This is an beta version, please report any bugs to the developer!");



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Passwords disabled!");
    }

    public void afterCheck(PasswordServer passwordServer, PasswordPlayer passwordPlayer) {
        // Plugin enable
        // Register Server Listeners
        passwordServer.listeners();
        // Register Player Listeners
        passwordPlayer.listeners();

    }

    public void reload(CommandSender sender) {
        Bukkit.getServer().getPluginManager().disablePlugin(this);

        Bukkit.getServer().getPluginManager().enablePlugin(this);

        sender.sendMessage("§9Reload done!");
    }

    public PasswordsApi getPasswordsApi() {
        if (getConfig().getBoolean("api.enable")) {
            return passwordsApi;
        } else {
            return null;
        }
    }

    
}
