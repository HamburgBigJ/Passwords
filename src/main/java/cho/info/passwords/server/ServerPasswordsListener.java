package cho.info.passwords.server;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.ConfigManager;
import cho.info.passwords.utls.Massages;
import net.kyori.adventure.text.Component;

import org.apache.maven.model.Site;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ServerPasswordsListener implements Listener {

    private final Passwords passwords;
    private final ConfigManager configManager;
    

    public ServerPasswordsListener(Passwords passwords, ConfigManager configManager) {
        this.passwords = passwords;
        this.configManager = configManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Sets isLogIn to false
        configManager.setPlayerValue(player, "isLogIn", false);

        for (int i = 0; i < 4; i++) {
            configManager.setPlayerValue(player, "char" + i, null);
        }

        configManager.setPlayerValue(player, "charSlot", 0);
        configManager.setPlayerValue(player, "password", null);
        
        buildInv(player);
    }

    public void buildInv(Player player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.CRAFTING, ChatColor.BLUE + "" + ChatColor.BOLD + "Passwords");
        ItemStack selectItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        for (int i = 0; i <= 9; i++) {
            ItemMeta itemMeta = selectItem.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(ChatColor.DARK_GREEN + "" + i);
                selectItem.setItemMeta(itemMeta);
                inventory.setItem(i, selectItem);
            } else {
                passwords.getLogger().info(ChatColor.RED + "[Error] ServerPasswordsListener.buildInv() ErrorCode: " + i);
            }
        }
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equals(ChatColor.BLUE + "" + ChatColor.BOLD + "Passwords")) {
            event.setCancelled(true);

            String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
            int charSlot = (int) configManager.getPlayerValue(player, "charSlot");

            if (charSlot < 4) {
                for (int i = 1; i <= 9; i++) {
                    if (displayName.equals(ChatColor.DARK_GREEN + "" + i)) {
                        configManager.setPlayerValue(player, "char" + charSlot, i);
                        configManager.setPlayerValue(player, "charSlot", charSlot + 1);

                        break;
                    }
                }
            }
            int codeCharSlot = (int) configManager.getPlayerValue(player, "charSlot");

            if (codeCharSlot == 4) {
                String password = (String) configManager.getPlayerValue(player, "password");

                
                String char0 = (String) configManager.getPlayerValue(player, "char0");
                String char1 = (String) configManager.getPlayerValue(player, "char1");
                String char2 = (String) configManager.getPlayerValue(player, "char2");
                String char3 = (String) configManager.getPlayerValue(player, "char3");

                password = char0 + char1 + char2 + char3;

                configManager.setPlayerValue(player, "password", password);

                if (password == passwords.getConfig().getString("server.password")) {
                        configManager.setPlayerValue(player, "isLogIn", true);
                        player.closeInventory();

                        if (passwords.getConfig().getBoolean("settings.welcome-massage-bool")) {
                            Massages massages = new Massages();
                            String wlecomeMessageType = passwords.getConfig().getString("settings.welcome-massage-display-type");
                            String welcmoeMessage = passwords.getConfig().getString("settings.welcome-massage");

                            if (wlecomeMessageType == "chat") {
                                massages.sendMessage(player, welcmoeMessage);
                            } else if (wlecomeMessageType == "titel") {
                                massages.sendActonBar(player, welcmoeMessage);

                            } else if (wlecomeMessageType == "actionbar") {
                                massages.sendActonBar(player, welcmoeMessage);

                            } else {
                                passwords.getLogger().info(ChatColor.RED + "[Error] cho.info.passwords.server.ServerPasswordListener.onInventoryClick : welcome-message-display-type null");
                            }
                        }

                    

                        
                } else {
                    player.kick(Component.text(ChatColor.RED + "" + ChatColor.BOLD + passwords.getConfig().getString("settings.fail-massage")));

                }
            }
        }


    }

    @EventHandler
    public void onInvetnoryClose(InventoryCloseEvent event) {

        if (event.getView().getTitle().equals(ChatColor.BLUE + "" + ChatColor.BOLD + "Passwords")) {
            Player player = (Player) event.getPlayer();
            Boolean isLogIn = (Boolean) configManager.getPlayerValue(player, "isLogIn");
    
            if (!isLogIn) {
                player.kick(Component.text(ChatColor.RED + "You need to enter the Password"));
    
            }
        }

    }
}
