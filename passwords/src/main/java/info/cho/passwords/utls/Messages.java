package info.cho.passwords.utls;

import info.cho.passwords.Passwords;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Messages {

    // TODO: better placeholder api alternative

    public static void sendActonBar(@NotNull Player player, String message) {
        player.sendActionBar(Component.text(Passwords.placeholders.applyPlaceholder(message, player)));
    }

    public static void sendTitle(@NotNull Player player, String message, String second) {
        player.showTitle(Title.title(Component.text(Passwords.placeholders.applyPlaceholder(message, player)), Component.text(Passwords.placeholders.applyPlaceholder(second, player))));
    }

    public static void sendMessage(@NotNull Player player, String message) {
        player.sendMessage(Passwords.placeholders.applyPlaceholder(message, player));
    }
}
