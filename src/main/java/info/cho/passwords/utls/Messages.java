package info.cho.passwords.utls;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Messages {

    public static void sendActonBar(@NotNull Player player, String message) {
        player.sendActionBar(Component.text(message));
    }

    public static void sendTitle(@NotNull Player player, String message, String second) {
        player.showTitle(Title.title(Component.text(message), Component.text(second)));
    }

    public static void sendMessage(@NotNull Player player, String message) {
        player.sendMessage(message);
    }
}
