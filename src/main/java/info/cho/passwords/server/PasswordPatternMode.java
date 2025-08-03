package info.cho.passwords.server;

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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PasswordPatternMode extends PasswordsGui {
    @Override
    public void openGui(PlayerJoinEvent event) {
        List<String> defaultPattern = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            defaultPattern.add("000");
        }
        getDataManager().addListValue(event.getPlayer(), "pattern", defaultPattern);


        PLog.debug("Pattern mode patter: " + PasswordConfig.getServerPatternList() );

        for (int i = 0; i < PasswordConfig.getServerPatternList().size(); i++) {
            PLog.debug("Pattern mode patter by list: " + PasswordConfig.getServerPatternList().get(i));
        }
        PLog.debug("Pattern mode pattern translated: " + getServerPattern());
        PLog.debug("Pattern mode patter length: " + getServerPatterLength());

        generateStdVariables(9, event.getPlayer());
    }

    @Override
    public void interactGui(InventoryClickEvent event) {
        if (event.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE) {
            PLog.debug("onGuiInteract");
        } else return;

        Player player = (Player) event.getWhoClicked();

        getDataManager().setPlayerValue(player, "char" + (event.getSlot() + 1),  1);
        getDataManager().setPlayerValue(player, "charLocation", (int) getDataManager().getPlayerValue(player, "charLocation") + 1);

        ItemStack item = event.getCurrentItem();
        ItemStack newItem = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(Component.text(("x"), NamedTextColor.DARK_GREEN));
            newItem.setItemMeta(itemMeta);
        }

        event.getInventory().setItem(event.getSlot(), newItem);


        if (getDataManager().getPlayerValue(player, "charLocation").equals(getServerPatterLength() + 1)) {
            List<Object> playerPattern = new ArrayList<>();
            for (int row = 0; row < 3; row++) {
                StringBuilder sb = new StringBuilder();
                for (int col = 1; col <= 3; col++) {
                    Object value = getDataManager().getPlayerValue(player, "char" + (row * 3 + col));
                    if (value == null || value.equals("n/a")) {
                        value = "0";
                    }
                    sb.append(String.valueOf(value));
                }
                playerPattern.add(sb.toString());
            }

            PLog.debug("Player pattern: " + playerPattern);
            getDataManager().setPlayerValue(player, "pattern", playerPattern);

            if (PasswordConfig.getServerPatternList().equals(playerPattern)) {
                getDataManager().setPlayerValue(player, "isLogin", true);
                player.closeInventory();

                welcomeMessage(player);
                gamemodeSwitch(player);

                PLog.debug("Login gamemode enabled");

            } else {
                kickPlayer(player);
            }

        }

    }

    @Override
    public void closeGui(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (!(boolean) getDataManager().getPlayerValue(player, "isLogin")) {
            kickPlayer(player);
        }
    }

    @Override
    public void playerQuit(PlayerQuitEvent event) {
        removeStaffPermissions(event.getPlayer());

    }

    @Override
    public Inventory getInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.DROPPER, Component.text(PasswordConfig.getGuiName()));

        ItemStack selectItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        for (int i = 0; i < 9; i++) {
            ItemMeta itemMeta = selectItem.getItemMeta();
            if (itemMeta != null) {
                itemMeta.displayName(Component.text(("o"), NamedTextColor.DARK_GREEN));
                selectItem.setItemMeta(itemMeta);
                inventory.setItem(i, selectItem);
            }
        }

        return inventory;
    }

    public List<Object> getServerPattern() {
        List<Object> serverPattern = PasswordConfig.getServerPatternList();
        serverPattern.replaceAll(s -> {
            if (s instanceof String str) {
                return str.replace('o', '0').replace('x', '1');
            } else {
                return "0";
            }
        });
        return serverPattern;
    }

    public int getServerPatterLength() {
        List<Object> serverPatternLength = getServerPattern();
        int count = 0;
        for (Object obj : serverPatternLength) {
            String str = obj.toString();
            for (char c : str.toCharArray()) {
                if (c == '1') count++;
            }
        }
        return count;
    }
}
