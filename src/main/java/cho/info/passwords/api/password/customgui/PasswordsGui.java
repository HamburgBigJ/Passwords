package cho.info.passwords.api.password.customgui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public abstract class PasswordsGui {

    /**
     * Open the gui on join.
     * @param event PlayerJoinEvent
     */
    public abstract void openGui(PlayerJoinEvent event);

    /**
     * Interact with the gui.
     * @param event InventoryClickEvent
     */
    public abstract void interactGui(InventoryClickEvent event);

    /**
     * Close gui event.
     * @param event InventoryCloseEvent
     */
    public abstract void closeGui(InventoryCloseEvent event);

}

