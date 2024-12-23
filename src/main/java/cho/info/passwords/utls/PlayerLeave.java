package cho.info.passwords.utls;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    public ConfigManager configManager;

    public PlayerLeave(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void playerLeaveEvent(PlayerQuitEvent event) {
        configManager.setPlayerValue(event.getPlayer(), "isLogIn", false);
    }

}
