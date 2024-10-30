package cho.info.passwords.utls;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Messages {

    public void sendActonBar(Player player, String message) {
        player.sendActionBar(ChatColor.BLUE +  message);
    }

    public void sendTitel(Player player, String message, String second) {
        player.sendTitle(ChatColor.BLUE + message,ChatColor.GOLD + second);
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.BLUE + message);
    }
}
