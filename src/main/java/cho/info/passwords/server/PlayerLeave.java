package cho.info.passwords.server;

import cho.info.passwords.Passwords;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void playerLeaveEvent(PlayerQuitEvent event) {
        Passwords.dataManager.setPlayerValue(event.getPlayer(), "isLogIn", false);
    }

}