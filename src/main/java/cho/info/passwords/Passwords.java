package cho.info.passwords;

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

public final class Passwords extends JavaPlugin {

    public ConfigManager configManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        ConfigManager configManager = new ConfigManager(getDataFolder());
        PasswordServer passwordServer = new PasswordServer(this, configManager);
        PasswordPlayer passwordPlayer = new PasswordPlayer(configManager, this);
        PublicCommands publicCommands = new PublicCommands(this);
        PlayerCommands playerCommands = new PlayerCommands(this, configManager);

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

        sender.sendMessage(ChatColor.BLUE + "Reload done!");
    }
}
