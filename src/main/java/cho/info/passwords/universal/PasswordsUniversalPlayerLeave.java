package cho.info.passwords.universal;

import cho.info.passwords.utls.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PasswordsUniversalPlayerLeave implements Listener {

    public ConfigManager configManager;

    public PasswordsUniversalPlayerLeave(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        configManager.setPlayerValue(player, "isLogIn", "false");
    }

}
