package info.cho.passwords.server;

import info.cho.passwords.Passwords;
import info.cho.passwords.utls.PLog;
import info.cho.passwordsApi.password.PasswordConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerRemPermissions implements Listener {

    public PlayerRemPermissions() {
        Passwords.instance.getServer().getPluginManager().registerEvents(this, Passwords.instance);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (!PasswordConfig.isRemoveStaffPermissionsOnLogout()) return;
        for (String permission : PasswordConfig.getStaffPermissions()) {
            event.getPlayer().addAttachment(Passwords.instance, permission, false);
            PLog.debug("PermissionRemove: " + permission);
        }
    }

}
