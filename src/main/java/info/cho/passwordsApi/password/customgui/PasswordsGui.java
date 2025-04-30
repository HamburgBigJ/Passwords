package info.cho.passwordsApi.password.customgui;

import info.cho.passwords.Passwords;
import info.cho.passwords.utls.DataManager;
import info.cho.passwords.utls.Messages;
import info.cho.passwords.utls.PLog;
import info.cho.passwordsApi.password.PasswordConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public abstract class PasswordsGui {

    private DataManager dataManager;

    public PasswordsGui() {
        this.dataManager = new DataManager();
    }

    /**
     * Get the data manager.
     * @return DataManager
     */
    public DataManager getDataManager() {
        return dataManager;
    }

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

    /**
     * Player quit event.
     * @param event PlayerQuitEvent
     */
    public abstract void playerQuit(PlayerQuitEvent event);

    /**
     * Get the inventory the player can see.
     * @return Inventory
     */
    public abstract Inventory getInventory(Player player);

    /**
     * Generate the variables for the player.
     * @param slots Number of slots
     * @param player Player
     */
    public void generateStdVariables(int slots, Player player) {
        dataManager.addValue(player, "charLocation", 1);
        dataManager.setPlayerValue(player, "charLocation", 1);
        for (int i = 1; i < slots; i++) {
            dataManager.addValue(player, "char" + i, "n/a");
            dataManager.setPlayerValue(player, "char" + i, "n/a");
        }
    }

    /**
     * Set the gamemode for the player.
     * @param player Player
     */
    public void gamemodeSwitch(Player player) {
        if (PasswordConfig.isLoginGamemodeEnabled()) {
            switch (PasswordConfig.getLoginGamemode()) {
                case "survival" -> player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                case "creative" -> player.setGameMode(org.bukkit.GameMode.CREATIVE);
                case "adventure" -> player.setGameMode(org.bukkit.GameMode.ADVENTURE);
                case "spectator" -> player.setGameMode(org.bukkit.GameMode.SPECTATOR);
                default -> {
                    player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                }
            }
        }
    }

    /**
     * Send the welcome message to the player.
     * @param player Player
     */
    public void welcomeMessage(Player player) {
        if (PasswordConfig.isWelcomeMessageEnabled()) {
            switch (PasswordConfig.getWelcomeMessageDisplayType()) {
                case "actionbar" -> Messages.sendActonBar(player, PasswordConfig.getWelcomeMessage());
                case "title" -> Messages.sendTitel(player, PasswordConfig.getWelcomeMessage(), PasswordConfig.getWelcomeMessageSecondLine());
                case "message" -> Messages.sendMessage(player, PasswordConfig.getWelcomeMessage());
            }
        }
    }

    /**
     * Remove permissions from the player.
     * @param player Player
     */
    public void removePermissions(Player player) {
        if (!PasswordConfig.isRemoveStaffPermissionsOnLogout()) return;
        for (String permission : PasswordConfig.getStaffPermissions()) {
            player.addAttachment(Passwords.instance, permission, false);
            PLog.debug("PermissionRemove: " + permission);
        }
    }
}

