package cho.info.passwords.utls;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Massages {

    public void sendActonBar(Player player, String message) {
        player.sendActionBar(ChatColor.BLUE +  message);
    }

    public void sendTitel(Player player, String message) {
        player.sendTitle(ChatColor.BLUE + message,ChatColor.GOLD + "Passwords");
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.BLUE + message);
    }
}
