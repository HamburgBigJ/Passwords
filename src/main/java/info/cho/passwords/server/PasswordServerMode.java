package info.cho.passwords.server;

import info.cho.passwords.utls.DataManager;
import info.cho.passwords.utls.Messages;
import info.cho.passwords.utls.PLog;
import info.cho.passwordsApi.password.PasswordConfig;
import info.cho.passwordsApi.password.customgui.PasswordsGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PasswordServerMode extends PasswordsGui {

    private DataManager dataManager;

    public PasswordServerMode() {
        this.dataManager = new DataManager();
    }


    @Override
    public void openGui(PlayerJoinEvent event) {
        generateVariables(PasswordConfig.getPasswordLength(), event.getPlayer());

    }

    @Override
    public void interactGui(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int passwordLength = PasswordConfig.getPasswordLength();
        PLog.debug("Password length: " + passwordLength);



        dataManager.setPlayerValue(player, "char" + (int) dataManager.getPlayerValue(player, "charLocation"), event.getSlot() + 1);
        dataManager.setPlayerValue(player, "charLocation", (int) dataManager.getPlayerValue(player, "charLocation") + 1);

        ItemStack item = event.getCurrentItem();
        ItemStack newItem = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(Component.text((event.getSlot() + 1), NamedTextColor.GREEN));
            newItem.setItemMeta(itemMeta);
        }

        event.getInventory().setItem(event.getSlot(), newItem);


        if ((int)dataManager.getPlayerValue(player, "charLocation") > passwordLength) {
            String password = "";
            for (int i = 1; i <= passwordLength; i++) {
                password += dataManager.getPlayerValue(player, "char" + i);
            }

            if (PasswordConfig.getServerPassword().equals(password)) {
                dataManager.setPlayerValue(player, "isLogin", true);
                player.closeInventory();

                if (PasswordConfig.isWelcomeMessageEnabled()) {
                    switch (PasswordConfig.getWelcomeMessageDisplayType()) {
                        case "actionbar" -> Messages.sendActonBar(player, PasswordConfig.getWelcomeMessage());
                        case "title" -> Messages.sendTitel(player, PasswordConfig.getWelcomeMessage(), PasswordConfig.getWelcomeMessageSecondLine());
                        case "message" -> Messages.sendMessage(player, PasswordConfig.getWelcomeMessage());
                    }
                }

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

                PLog.debug("Login gamemode enabled");

            } else {
                player.kick(Component.text(PasswordConfig.getFailMessage(), NamedTextColor.RED));
            }
        }


        event.setCancelled(true);
    }

    @Override
    public void closeGui(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if ((boolean) dataManager.getPlayerValue(player, "isLogin")) {

            return;
        }
        player.kick(Component.text(PasswordConfig.getCloseUiMessage(), NamedTextColor.RED));
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.DROPPER, Component.text(PasswordConfig.getGuiName()));

        ItemStack selectItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        for (int i = 0; i < 9; i++) {
            ItemMeta itemMeta = selectItem.getItemMeta();
            if (itemMeta != null) {
                itemMeta.displayName(Component.text((i + 1), NamedTextColor.GREEN));
                selectItem.setItemMeta(itemMeta);
                inventory.setItem(i, selectItem);
            }
        }

        return inventory;
    }

    private void generateVariables(int slots, Player player) {
        dataManager.addValue(player, "isLogin", false);
        dataManager.addValue(player, "isLogin", false);
        dataManager.addValue(player, "charLocation", 1);
        dataManager.setPlayerValue(player, "charLocation", 1);
        for (int i = 1; i < slots; i++) {
            dataManager.addValue(player, "char" + i, "n/a");
            dataManager.setPlayerValue(player, "char" + i, "n/a");
        }
    }
}
