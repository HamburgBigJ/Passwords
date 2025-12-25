package info.cho.passwords.utls;

import info.cho.passwords.Passwords;
import info.cho.passwordsApi.password.PasswordConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class PlayerInventorySave {

    public static void savePlayerInventory() {
        PLog.debug("Saving player inventory initialization...");

        int intervalSeconds = PasswordConfig.getSavePlayerInventoryIntervall() * 60;
        int intervalTicks = intervalSeconds * 20; // Seconds to ticks (20 ticks per second)
        PLog.debug("Saving player inventory interval: " + intervalSeconds + " seconds (" + intervalTicks + " ticks)");

        getServer().getScheduler().runTaskTimer(Passwords.instance, () -> {
            PLog.debug("Saving player inventory...");
            for (Player player : Bukkit.getOnlinePlayers()) {
                DataManager dataManager = new DataManager();

                Object loginValue = dataManager.getPlayerValue(player, "isLogin");
                boolean isLoggedIn = loginValue != null && Boolean.parseBoolean(loginValue.toString());
                if (!isLoggedIn) {
                    continue;
                }

                Inventory playerInventory = player.getInventory();
                dataManager.setPlayerValue(player, "playerInventory", new ArrayList<>());

                for (int i = 0; i < playerInventory.getSize(); i++) {
                    if (playerInventory.getItem(i) != null) {
                        ItemStack currentItem = playerInventory.getItem(i);
                        List<Object> playerInventoryList = dataManager.getListValue(player, "playerInventory");
                        playerInventoryList.add(currentItem);
                        dataManager.setPlayerValue(player, "playerInventory", playerInventoryList);
                    } else {
                        ItemStack air = new ItemStack(Material.AIR);
                        List<Object> playerInventoryList = dataManager.getListValue(player, "playerInventory");
                        playerInventoryList.add(air);
                        dataManager.setPlayerValue(player, "playerInventory", playerInventoryList);
                    }
                }
            }
        }, intervalTicks, intervalTicks);

    }

}
