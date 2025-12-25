package info.cho.passwordsApi.placeholders;

import org.bukkit.entity.Player;

public abstract class CustomPlaceholder {
    public abstract String keyId();
    public abstract String resultKey(Player player);
}
