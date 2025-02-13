package cho.info.passwords;

import cho.info.horizonLib.plugin.HorizonPlugin;
import cho.info.passwords.api.PasswordsApi;
import cho.info.passwords.discord.DiscordListener;
import cho.info.passwords.player.PasswordPlayer;
import cho.info.passwords.player.commands.PlayerCommands;
import cho.info.passwords.publicCommands.PublicCommands;
import cho.info.passwords.server.PasswordServer;
import cho.info.passwords.utls.DataManager;
import cho.info.passwords.server.PlayerLeave;
import github.scarsz.discordsrv.DiscordSRV;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Passwords extends JavaPlugin {

    public DataManager dataManager;
    public String version = "1.8";

    public PasswordsApi passwordsApi;

    @Override
    public void onEnable() {
        // Plugin startup logic

        DataManager dataManager = new DataManager(getDataFolder());
        PasswordServer passwordServer = new PasswordServer(this, dataManager);
        PasswordPlayer passwordPlayer = new PasswordPlayer(dataManager, this);
        PublicCommands publicCommands = new PublicCommands(this);
        PlayerCommands playerCommands = new PlayerCommands(this, dataManager);

        PluginManager pluginManager = getServer().getPluginManager();

        this.passwordsApi = new PasswordsApi(this, dataManager);

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

        configUpdate();


        if (getConfig().getBoolean("api.enable")) {
            getLogger().info("API enabled!");

        } else {
            getLogger().info("API disabled!");
        }

        getLogger().info("Your are currently running version: " + version);
        getLogger().info("This is an beta version, please report any bugs to the developer!");



        pluginManager.registerEvents(new PlayerLeave(dataManager), this);



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

    public void configUpdate() {
        if (!getConfig().getBoolean("auto-update")) return;

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

    }

    public void pluginIntegration(PluginManager pluginManager) {

        // Discord Srv
        if (Bukkit.getServer().getPluginManager().getPlugin("DiscordSRV") != null) {
            getLogger().info("DiscordSRV found!");
            if (getConfig().getBoolean("settings.use-discord-srv")) {
                getLogger().info("DiscordSRV features enabled!");

                // DiscordSRV features

                DiscordSRV.api.subscribe(this);

                pluginManager.registerEvents(new DiscordListener(dataManager, this), this);

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
