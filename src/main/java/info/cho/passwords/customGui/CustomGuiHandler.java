package info.cho.passwords.customGui;

import info.cho.passwords.Passwords;
import info.cho.passwords.utls.DataManager;
import info.cho.passwords.utls.PLog;
import info.cho.passwordsApi.password.PasswordConfig;
import info.cho.passwordsApi.password.customgui.PasswordsGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;

public class CustomGuiHandler implements Listener {

    private final CustomGui customGui;

    public CustomGuiHandler(CustomGui customGui) {
        Passwords passwords = Passwords.instance;
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

                    if (passwordGui.getInventory(event.getPlayer()) == null) {
                        PLog.debug("Password GUI is null, returning.");
                        return;
                    }

                    Inventory inv = passwordGui.getInventory(event.getPlayer());
                    if (inv != null) {
                        if (PasswordConfig.invulnerableOnLogin()) {
                            event.getPlayer().setInvulnerable(true);
                        }

                        event.getPlayer().openInventory(inv);
                        PLog.debug("onGuiOpen end");
                    } else {
                        PLog.debug("Inventory is null, skipping openInventory.");
                    }

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
                    if (!event.getView().title().equals(Component.text(PasswordConfig.getGuiName()))) {
                        return;
                    }


                    DataManager dataManager = new DataManager();
                    PLog.debug("Player login test");
                    if ((boolean) dataManager.getPlayerValue((Player) event.getWhoClicked(), "isLogin")) {
                        PLog.debug("Player is already logged in");
                        continue;
                    }

                    PasswordsGui passwordGui = (PasswordsGui) entry.getValue().getDeclaredConstructor().newInstance();
                    passwordGui.interactGui(event);
                    event.setCancelled(true);

                    PLog.debug("onGuiInteract end");
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

                    if (PasswordConfig.invulnerableOnLogin()) {
                        event.getPlayer().setInvulnerable(false);
                    }

                    PLog.debug("onGuiClose end");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerQuit(PlayerQuitEvent event) {
        PLog.debug("playerQuit");
        for (Map.Entry<String, Class<?>> entry : customGui.customGuiList.entrySet()) {
            if (Objects.equals(PasswordConfig.getCheckType(), entry.getKey())) {
                try {
                    DataManager dataManager = new DataManager();
                    if (Objects.equals(dataManager.getPlayerValue(event.getPlayer(), "isLogin").toString(), "false")) {
                        PLog.debug("Player is not logged in");
                        return;
                    }
                    PasswordsGui passwordGui = (PasswordsGui) entry.getValue().getDeclaredConstructor().newInstance();
                    passwordGui.playerQuit(event);
                    PLog.debug("playerQuit end");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}