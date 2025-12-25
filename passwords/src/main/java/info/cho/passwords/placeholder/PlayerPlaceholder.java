package info.cho.passwords.placeholder;

import info.cho.passwordsApi.placeholders.CustomPlaceholder;
import org.bukkit.entity.Player;

public class PlayerPlaceholder extends CustomPlaceholder {
    @Override
    public String keyId() {
        return "&player&";
    }

    @Override
    public String resultKey(Player player) {
        return player.getName();
    }
}
