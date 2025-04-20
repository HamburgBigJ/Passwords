package cho.info.passwords.discord;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.DataManager;
import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class DiscordListener implements Listener {


    public boolean isLinked(Player player) {
        String discordID = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
        if (discordID != null) {
            return true;
        }else {
            return false;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoinEvent(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (!Passwords.instance.getConfig().getBoolean("discord.need-password")) {
            if (isLinked(player)) {
                Passwords.dataManager.setPlayerValue(player, "isLogIn", true);
            }
        }

    }

}