package info.cho.passwords.hooks;

import github.scarsz.discordsrv.DiscordSRV;
import info.cho.passwords.Passwords;
import info.cho.passwords.discord.DiscordListener;
import io.fairyproject.log.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class DiscordSrvHook {

    public DiscordSrvHook() {
        // Discord Srv
        if (Bukkit.getServer().getPluginManager().getPlugin("DiscordSRV") != null) {
            Log.info("DiscordSRV found!");
            if (Passwords.config.isDiscordNeedPassword()) {
                Log.info("DiscordSRV features enabled!");

                // DiscordSRV features

                DiscordSRV.api.subscribe(this);
                PluginManager pm = Bukkit.getServer().getPluginManager();
                pm.registerEvents(new DiscordListener(), (Plugin) Passwords.instance);

            } else {
                Log.info("DiscordSRV features disabled!");
            }
        }else{
            Log.info("DiscordSRV not found!");
            Log.info("DiscordSRV features disabled!");
        }
    }

}
