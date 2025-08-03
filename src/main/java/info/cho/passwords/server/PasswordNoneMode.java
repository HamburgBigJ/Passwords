package info.cho.passwords.server;

import info.cho.passwords.utls.PLog;
import info.cho.passwordsApi.password.PasswordConfig;
import info.cho.passwordsApi.password.customgui.PasswordsGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class PasswordNoneMode extends PasswordsGui {
    @Override
    public void openGui(PlayerJoinEvent event) {
        PLog.debug("Opening Passwords GUI none");

    }

    @Override
    public void interactGui(InventoryClickEvent event) {

    }

    @Override
    public void closeGui(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        welcomeMessage(player);
    }

    @Override
    public void playerQuit(PlayerQuitEvent event) {
        return;
    }

    @Override
    public Inventory getInventory(Player player) {
        return null;
    }
}
