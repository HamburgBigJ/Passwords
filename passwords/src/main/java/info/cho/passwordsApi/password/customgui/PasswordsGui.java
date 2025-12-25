package info.cho.passwordsApi.password.customgui;

import info.cho.passwordsApi.password.PasswordConfig;
import info.cho.passwords.Passwords;
import info.cho.passwords.utls.DataManager;
import info.cho.passwords.utls.Messages;
import info.cho.passwords.utls.PLog;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class PasswordsGui {

    private final DataManager dataManager;

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
                case "title" -> Messages.sendTitle(player, PasswordConfig.getWelcomeMessage(), PasswordConfig.getWelcomeMessageSecondLine());
                case "message" -> Messages.sendMessage(player, PasswordConfig.getWelcomeMessage());
            }
        }
    }

    /**
     * Remove staff permissions from the player.
     * @param player Player
     */
    public void removeStaffPermissions(Player player) {
        if (!PasswordConfig.isRemoveStaffPermissionsOnLogout()) return;
        for (String permission : PasswordConfig.getStaffPermissions()) {
            player.addAttachment(Passwords.instance, permission, false);
            PLog.debug("PermissionRemove: " + permission);
        }
    }

    /**
     * Kick the player with a fail message.
     * @param player Player
     */
    public void kickPlayer(Player player) {
        player.kick(Component.text(
                PasswordConfig.getFailMessage()
        ).color(NamedTextColor.RED));
    }

    /**
     * Save the player's inventory to the data manager.
     * @param player Player
     */
    public void savePlayerInventory(Player player) {
        if (!PasswordConfig.isSavePlayerInventory()) return;
        dataManager.setPlayerValue(player, "playerInventory", new ArrayList<>());
        PLog.debug("Saving inventory for player: " + player.getName());

        Inventory playerInventory = player.getInventory();

        for (int i = 0; i < playerInventory.getSize(); i++) {
            if (playerInventory.getItem(i) != null) {
                String itemName = playerInventory.getItem(i).getType().name();
                int itemAmount = playerInventory.getItem(i).getAmount();
                PLog.debug("Saving item: " + itemName + " with amount: " + itemAmount + " at slot: " + i);

                ItemStack currentItem = playerInventory.getItem(i);
                List<Object> playerInventoryList = getDataManager().getListValue(player, "playerInventory");
                playerInventoryList.add(currentItem);
                dataManager.setPlayerValue(player, "playerInventory", playerInventoryList);
            } else {
                PLog.debug("Slot " + i + " is empty.");
                ItemStack air = new ItemStack(Material.AIR);
                List<Object> playerInventoryList = getDataManager().getListValue(player, "playerInventory");
                playerInventoryList.add(air);
                dataManager.setPlayerValue(player, "playerInventory", playerInventoryList);
            }
        }

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            player.getInventory().setItem(i, new ItemStack(Material.AIR));
        }
    }

    /**
     * Loads the saved inventory in player data
     * @param player Player
     */
    public void loadPlayerInventory(Player player) {
        if (!PasswordConfig.isSavePlayerInventory()) return;
        PLog.debug("Loading inventory for player: " + player.getName());

        List<Object> playerInventory = dataManager.getListValue(player, "playerInventory");

        for (int i = 0; i < playerInventory.size(); i++) {
            if (playerInventory.get(i) instanceof ItemStack itemStack) {
                if (itemStack.getType() == Material.AIR) {
                    continue;
                }
                PLog.debug("Loading item: " + itemStack.getType().name() + " with amount: " + itemStack.getAmount() + " at slot: " + i);
                player.getInventory().setItem(i, itemStack);
            } else {
                PLog.debug("Slot " + i + " is empty.");
            }
        }

    }
}

