package info.cho.passwords.customgui;

import info.cho.passwords.Passwords;
import info.cho.passwords.config.Config;
import info.cho.passwords.util.DataManager;
import info.cho.passwordsapi.PasswordApi;
import info.cho.passwordsapi.customGui.PasswordsGui;
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
    public Config config;

    public CustomGuiHandler() {
        this.passwords = Passwords.instance;
        this.dataManager = Passwords.dataManager;
        this.config = Passwords.config;
    }

    @EventHandler
    public void onGuiOpen(PlayerJoinEvent event) {
        if (config.getCheckType().equals("custom")) {
            event.getPlayer().openInventory(passwordGui.getInventory());
            passwordGui.openGui(event);
        }
    }


    @EventHandler
    public void onGuiInteract(InventoryClickEvent event) {
        if (config.getCheckType().equals("custom")) {
            passwordGui.interactGui(event);
        }
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (config.getCheckType().equals("custom")) {
            passwordGui.closeGui(event);
            if (event.getView().getTitle().equals(config.getGuiName())) {
                Player player = (Player) event.getPlayer();
                Boolean isLogIn = (Boolean) dataManager.getPlayerValue(player, "isLogIn");

                if (!isLogIn) {
                    player.kickPlayer(config.getCloseUiMessage());
                }
            }
        }

    }


    @EventHandler
    public void onMovementCheck(PlayerMoveEvent event) {
        if (config.getCheckType().equals("custom")) {
            Boolean preventMovement = config.isPreventsMovement();

            if (preventMovement) {
                Boolean isLogIn = (Boolean) dataManager.getPlayerValue(event.getPlayer(), "isLogIn");

                if (!isLogIn) {
                    event.setCancelled(true);
                    event.getPlayer().kickPlayer(config.getMessageKickMovement());
                }
            }
        }
    }

}