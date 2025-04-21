package info.cho.passwords.utls;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

public class Messages {

    public void sendActonBar(Player player, String message) {
        player.sendActionBar(Component.text(message));
    }

    public void sendTitel(Player player, String message, String second) {
        player.showTitle(Title.title(Component.text(message), Component.text(second)));
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(message);
    }
}
