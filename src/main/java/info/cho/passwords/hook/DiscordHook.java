package info.cho.passwords.hook;

import github.scarsz.discordsrv.DiscordSRV;
import info.cho.passwords.Passwords;
import info.cho.passwords.utls.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class DiscordHook implements Listener {

    // Experimental

    public DiscordHook() {

        Passwords.instance.getServer().getPluginManager().registerEvents(this, Passwords.instance);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLogin(PlayerJoinEvent event) {
        if (Bukkit.getPluginManager().isPluginEnabled("DiscordSRV")) {
            Player player = event.getPlayer();

            if (!Passwords.instance.getConfig().getBoolean("discord.need-password")) {
                if (isLinked(player)) {
                    DataManager dataManager = new DataManager();
                    dataManager.setPlayerValue(player, "isLogIn", true);
                }
            }
        }
    }

    public boolean isLinked(Player player) {
        String discordID = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
        if (discordID != null) {
            return true;
        }else {
            return false;
        }
    }
}
