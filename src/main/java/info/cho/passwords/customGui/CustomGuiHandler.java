package info.cho.passwords.customGui;

import info.cho.passwords.Passwords;
import info.cho.passwords.utls.DataManager;
import info.cho.passwords.utls.PLog;
import info.cho.passwordsApi.password.PasswordConfig;
import info.cho.passwordsApi.password.customgui.PasswordsGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;
import java.util.Objects;

public class CustomGuiHandler implements Listener {

    private final Passwords passwords;
    private final CustomGui customGui;

    public CustomGuiHandler(CustomGui customGui) {
        this.passwords = Passwords.instance;
        this.customGui = customGui;

        passwords.getServer().getPluginManager().registerEvents(this, passwords);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGuiOpen(PlayerJoinEvent event) {
        // Basic Variables
        DataManager dataManager = new DataManager();
        dataManager.addValue(event.getPlayer(), "isLogin", false);
        dataManager.setPlayerValue(event.getPlayer(), "isLogin", false);
        dataManager.addValue(event.getPlayer(), "password", "n/a");

        PLog.debug("onGuiOpen");
        for (Map.Entry<String, Class<?>> entry : customGui.customGuiList.entrySet()) {
            if (Objects.equals(PasswordConfig.getCheckType(), entry.getKey())) {
                try {
                    if (event.getPlayer().isDead()) {
                        event.getPlayer().spigot().respawn();
                    }

                    PasswordsGui passwordGui = (PasswordsGui) entry.getValue().getDeclaredConstructor().newInstance();
                    passwordGui.openGui(event);
                    event.getPlayer().openInventory(passwordGui.getInventory(event.getPlayer()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGuiInteract(InventoryClickEvent event) {
        PLog.debug("onGuiInteract");
        for (Map.Entry<String, Class<?>> entry : customGui.customGuiList.entrySet()) {
            if (Objects.equals(PasswordConfig.getCheckType(), entry.getKey())) {
                try {
                    PasswordsGui passwordGui = (PasswordsGui) entry.getValue().getDeclaredConstructor().newInstance();
                    passwordGui.interactGui(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGuiClose(InventoryCloseEvent event) {
        PLog.debug("onGuiClose");
        for (Map.Entry<String, Class<?>> entry : customGui.customGuiList.entrySet()) {
            if (Objects.equals(PasswordConfig.getCheckType(), entry.getKey())) {
                try {
                    PasswordsGui passwordGui = (PasswordsGui) entry.getValue().getDeclaredConstructor().newInstance();
                    passwordGui.closeGui(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}