package cho.info.passwords.server.customgui;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.PLog;
import cho.info.passwordsApi.password.customgui.PasswordsGui;
import cho.info.passwords.utls.DataManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class CustomGuiHandler implements Listener {

    public Passwords passwords;
    public DataManager dataManager;
    public PasswordsGui passwordGui;

    public CustomGuiHandler() {
        this.passwords = Passwords.instance;
        this.dataManager = Passwords.dataManager;
    }

    @EventHandler
    public void onGuiOpen(PlayerJoinEvent event) {
        if (passwords.getConfig().getString("settings.check-type").equals("custom")) {
            PLog.debug(event.getPlayer().getName() + " has been opened");
            event.getPlayer().openInventory(passwordGui.getInventory());
            passwordGui.openGui(event);
        }
    }


    @EventHandler
    public void onGuiInteract(InventoryClickEvent event) {
        if (passwords.getConfig().getString("settings.check-type").equals("custom")) {
            PLog.debug(event.getWhoClicked().getName() + " has been clicked");
            passwordGui.interactGui(event);
        }
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (passwords.getConfig().getString("settings.check-type").equals("custom")) {
            passwordGui.closeGui(event);
            if (event.getView().getTitle().equals(passwords.getConfig().getString("settings.gui-name"))) {
                Player player = (Player) event.getPlayer();
                Boolean isLogIn = (Boolean) dataManager.getPlayerValue(player, "isLogIn");

                if (!isLogIn) {
                    player.kick(Component.text(Objects.requireNonNull(passwords.getConfig().getString("settings.close-ui-message"))));
                    PLog.debug(player.getName() + " has been closed");
                }
            }
        }

    }


    @EventHandler
    public void onMovementCheck(PlayerMoveEvent event) {
        if (passwords.getConfig().getString("settings.check-type").equals("custom")) {
            Boolean preventMovement = passwords.getConfig().getBoolean("settings.prevents-movement");

            if (preventMovement) {
                Boolean isLogIn = (Boolean) dataManager.getPlayerValue(event.getPlayer(), "isLogIn");

                if (!isLogIn) {
                    event.setCancelled(true);
                    event.getPlayer().kick(Component.text(Objects.requireNonNull(passwords.getConfig().getString("settings.message-kick-movement"))));
                    PLog.debug(event.getPlayer().getName() + " has been closed");
                }
            }
        }
    }

}