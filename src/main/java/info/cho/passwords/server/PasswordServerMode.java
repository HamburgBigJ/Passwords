package info.cho.passwords.server;

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


    @Override
    public void openGui(PlayerJoinEvent event) {

    }

    @Override
    public void interactGui(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void closeGui(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
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
}
