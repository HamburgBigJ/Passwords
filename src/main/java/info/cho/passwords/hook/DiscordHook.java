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

    public DiscordHook() {

        Passwords.instance.getServer().getPluginManager().registerEvents(this, Passwords.instance);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLogin(PlayerJoinEvent event) {
        if (!Bukkit.getPluginManager().isPluginEnabled("DiscordSRV")) return;
        if (!Passwords.instance.getConfig().getBoolean("discord.need-password")) {
            if (isLinked(event.getPlayer())) {
                DataManager dataManager = new DataManager();
                dataManager.setPlayerValue(event.getPlayer(), "isLogIn", true);
            }
        }

    }

    public boolean isLinked(Player player) {
        String discordID = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
        return discordID != null;
    }
}
