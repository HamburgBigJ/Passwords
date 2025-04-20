package cho.info.passwords;

import cho.info.passwordsApi.PasswordsApi;
import cho.info.passwords.discord.DiscordListener;
import cho.info.passwords.player.PasswordPlayer;
import cho.info.passwords.player.commands.PlayerCommands;
import cho.info.passwords.publicCommands.PublicCommands;
import cho.info.passwords.server.PasswordServer;
import cho.info.passwords.utls.DataManager;
import github.scarsz.discordsrv.DiscordSRV;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Passwords extends JavaPlugin {

    public static Passwords instance;

    public static DataManager dataManager;
    public static String version = "2.2-hotfix";

    public PasswordsApi passwordsApi;

    @Override
    public void onLoad() {
        instance = this;
        dataManager = new DataManager();
    }


    @Override
    public void onEnable() {


        PasswordServer passwordServer = new PasswordServer();
        PasswordPlayer passwordPlayer = new PasswordPlayer();
        PublicCommands publicCommands = new PublicCommands();
        PlayerCommands playerCommands = new PlayerCommands();

        PluginManager pluginManager = getServer().getPluginManager();

        this.passwordsApi = new PasswordsApi();

        getLogger().info("Passwords enabled!");
        saveDefaultConfig();

        pluginIntegration(pluginManager);

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



        if (getConfig().getBoolean("api.enable")) {
            getLogger().info("API enabled!");

        } else {
            getLogger().info("API disabled!");
        }

        getLogger().info("Your are currently running version: " + version);

        // Check for updates
        if(!Objects.equals(version, getConfig().getString("version"))) {
            getLogger().info("Your version is outdated! Please delete the config.yml and restart the server to get the latest version!");
            getLogger().info("Your version: " + version);
            getLogger().info("Latest version: " + getConfig().getString("version"));
        } else {
            getLogger().info("Your version is up to date!");
        }





        if (!getConfig().getBoolean("api.enable")) {
            getLogger().info("API is disabled!");
            getLogger().info("If you want to use the api feature, please enable it in the config.yml");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Passwords disabled!");


        if (Bukkit.getServer().getPluginManager().getPlugin("DiscordSRV") != null) {
            DiscordSRV.api.unsubscribe(this);
            getLogger().info("DiscordSRV features disabled!");
        }

    }

    private void afterCheck(PasswordServer passwordServer, PasswordPlayer passwordPlayer) {
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

    private void pluginIntegration(PluginManager pluginManager) {

        // Discord Srv
        if (Bukkit.getServer().getPluginManager().getPlugin("DiscordSRV") != null) {
            getLogger().info("DiscordSRV found!");
            if (getConfig().getBoolean("settings.use-discord-srv")) {
                getLogger().info("DiscordSRV features enabled!");

                // DiscordSRV features

                DiscordSRV.api.subscribe(this);

                pluginManager.registerEvents(new DiscordListener(), this);

            } else {
                getLogger().info("DiscordSRV features disabled!");
            }
        }else{
            getLogger().info("DiscordSRV not found!");
            getLogger().info("DiscordSRV features disabled!");
        }


    }

    // Api Stuff

    /**
     * @return Passwords Api
     */
    public PasswordsApi getPasswordsApi() {
        if (getConfig().getBoolean("api.enable")) {
            return this.passwordsApi;
        } else {
            return null;
        }
    }

    public Inventory getInventory() {
        return Bukkit.createInventory(null, InventoryType.DISPENSER, Component.text(Objects.requireNonNull(getConfig().getString("settings.gui-name"))));
    }

    public Inventory getFirstJoinInventory() {
        return Bukkit.createInventory(null, InventoryType.DISPENSER, Component.text(Objects.requireNonNull(getConfig().getString("settings.set-password-name"))));
    }

    
}
