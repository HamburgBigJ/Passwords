package info.cho.passwords.customGui;

import info.cho.passwords.Passwords;
import info.cho.passwords.utls.DataManager;
import info.cho.passwords.utls.PLog;
import info.cho.passwordsApi.password.PasswordConfig;
import info.cho.passwordsApi.password.customgui.PasswordsGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class CustomGuiHandler implements Listener {

    private final CustomGui customGui;
    private PasswordsGui passwordsGui; // no new instance for every event for static variables in gui

    public CustomGuiHandler() {
        this.customGui = Passwords.customGui;
        Passwords.instance.getServer().getPluginManager().registerEvents(this, Passwords.instance);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGuiOpen(PlayerJoinEvent event) {
        setCurrentMode();

        // Basic Variables
        DataManager dataManager = new DataManager();
        dataManager.addValue(event.getPlayer(), "isLogin", false);
        dataManager.setPlayerValue(event.getPlayer(), "isLogin", false);
        dataManager.addValue(event.getPlayer(), "password", "n/a");

        PLog.debug("onGuiOpen");
        /*for (Map.Entry<String, Class<?>> entry : customGui.customGuiList.entrySet()) { // Wy a foor loop use a fucking the fucking Hasmap
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
        }*/


        if (event.getPlayer().isDead()) {
            event.getPlayer().spigot().respawn();
        }

        passwordsGui.openGui(event);

        if (passwordsGui.getInventory(event.getPlayer()) == null) {
            PLog.debug("Password GUI is null, returning.");
            return;
        }

        Inventory inv = passwordsGui.getInventory(event.getPlayer());
        if (inv != null) {
            if (PasswordConfig.invulnerableOnLogin()) {
                event.getPlayer().setInvulnerable(true);
            }

            event.getPlayer().openInventory(inv);
            PLog.debug("onGuiOpen end");
        } else {
            PLog.debug("Inventory is null, skipping openInventory.");
        }


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGuiInteract(InventoryClickEvent event) {
        PLog.debug("onGuiInteract");

        /*for (Map.Entry<String, Class<?>> entry : customGui.customGuiList.entrySet()) { // TODO: Remove
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
        }*/


        // Dot use items for cheking
        if (!event.getView().title().equals(Component.text(PasswordConfig.getGuiName()))) {
            if (!event.getView().title().equals(Component.text(PasswordConfig.getSetPasswordName()))) {
                return;
            }
        }


        DataManager dataManager = new DataManager();
        PLog.debug("Player login test");
        if ((boolean) dataManager.getPlayerValue((Player) event.getWhoClicked(), "isLogin")) {
            PLog.debug("Player is already logged in");
        }

        passwordsGui.interactGui(event);
        event.setCancelled(true);

        PLog.debug("onGuiInteract end");

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGuiClose(InventoryCloseEvent event) {
        PLog.debug("onGuiClose");
        /*for (Map.Entry<String, Class<?>> entry : customGui.customGuiList.entrySet()) {
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
        }*/


        passwordsGui.closeGui(event);

        if (PasswordConfig.invulnerableOnLogin()) {
            event.getPlayer().setInvulnerable(false);
        }

        PLog.debug("onGuiClose end");

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerQuit(PlayerQuitEvent event) {
        PLog.debug("playerQuit");
        /*for (Map.Entry<String, Class<?>> entry : customGui.customGuiList.entrySet()) {
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
        }*/

        DataManager dataManager = new DataManager();
        if (Objects.equals(dataManager.getPlayerValue(event.getPlayer(), "isLogin").toString(), "false")) {
            PLog.debug("Player is not logged in");
            return;
        }
        passwordsGui.playerQuit(event);
        PLog.debug("playerQuit end");
    }

    public void setCurrentMode() {
        try {
            passwordsGui = (PasswordsGui) customGui.customGuiList.get(PasswordConfig.getCheckType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}