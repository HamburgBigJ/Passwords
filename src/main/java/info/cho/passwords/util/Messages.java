package info.cho.passwords.util;

import io.fairyproject.container.InjectableComponent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

@InjectableComponent
public class Messages {

    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(title, subtitle, 10, 70, 20);
    }

    public void sendMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.CHAT, new TextComponent(message));
    }
}