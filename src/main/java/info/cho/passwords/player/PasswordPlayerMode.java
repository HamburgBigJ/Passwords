package info.cho.passwords.player;

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

public class PasswordPlayerMode extends PasswordsGui {


    @Override
    public void openGui(PlayerJoinEvent event) {
        generateStdVariables(PasswordConfig.getPasswordLength(), event.getPlayer());

    }

    @Override
    public void interactGui(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int passwordLength = PasswordConfig.getPlayerPasswordLength();
        PLog.debug("Password length: " + passwordLength);
        boolean hasPassword;

        if (getDataManager().getPlayerValue(player, "password").equals("n/a")) {
            hasPassword = false;
        } else {
            hasPassword = true;
        }



        getDataManager().setPlayerValue(player, "char" + (int) getDataManager().getPlayerValue(player, "charLocation"), event.getSlot() + 1);
        getDataManager().setPlayerValue(player, "charLocation", (int) getDataManager().getPlayerValue(player, "charLocation") + 1);

        ItemStack item = event.getCurrentItem();
        ItemStack newItem = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(Component.text((event.getSlot() + 1), NamedTextColor.GREEN));
            newItem.setItemMeta(itemMeta);
        }

        event.getInventory().setItem(event.getSlot(), newItem);


        if ((int)getDataManager().getPlayerValue(player, "charLocation") > passwordLength) {
            String password = "";
            for (int i = 1; i <= passwordLength; i++) {
                password += getDataManager().getPlayerValue(player, "char" + i);
            }

            if (!hasPassword) {
                getDataManager().setPlayerValue(player, "password", password);
                getDataManager().setPlayerValue(player, "isLogin", true);
                player.closeInventory();

                welcomeMessage(player);
                gamemodeSwitch(player);

                PLog.debug("Player password set to: " + password);
                return;
            }

            if (getDataManager().getPlayerValue(player, "password").equals(password)) {
                getDataManager().setPlayerValue(player, "isLogin", true);
                player.closeInventory();

                welcomeMessage(player);
                gamemodeSwitch(player);

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
        if ((boolean) getDataManager().getPlayerValue(player, "isLogin")) {

            return;
        }
        player.kick(Component.text(PasswordConfig.getCloseUiMessage(), NamedTextColor.RED));
    }

    @Override
    public Inventory getInventory(Player player) {
        Inventory inventory;
        if (getDataManager().getPlayerValue(player, "password").equals("n/a")) {
            inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, Component.text(PasswordConfig.getSetPasswordName()));
        } else{
            inventory = Bukkit.createInventory(null, InventoryType.DROPPER, Component.text(PasswordConfig.getGuiName()));
        }


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


}
