package cho.info.passwords.server;

import cho.info.passwords.utls.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    public DataManager dataManager;

    public PlayerLeave(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void playerLeaveEvent(PlayerQuitEvent event) {
        dataManager.setPlayerValue(event.getPlayer(), "isLogIn", false);
    }

}