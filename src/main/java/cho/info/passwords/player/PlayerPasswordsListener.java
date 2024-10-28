package cho.info.passwords.player;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.ConfigManager;
import cho.info.passwords.utls.Massages;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerPasswordsListener implements Listener {

    public Passwords passwords;
    public ConfigManager configManager;
    public Massages massages;

    public PlayerPasswordsListener(Passwords passwords, ConfigManager configManager) {
        this.passwords = passwords;
        this.configManager = configManager;
    }

    @EventHandler
    public void playerPasswords(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (passwords.getConfig().getString("settings.check-type").equals("player")) {
            configManager.setPlayerValue(player, "isLogIn", "false");
            if (passwords.getConfig().getString("player." + player.getName() + ".password") == null) {
                player.kick(Component.text(ChatColor.RED + "Ask an Admin to add you to the Password List"));
            } else {
                buildInv(player);
            }

        }

    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Passwords")) {
            Player player = (Player) event.getPlayer();
            boolean isLogIn = (boolean) configManager.getPlayerValue(player ,"isLogIn");
            if (!isLogIn) {
                buildInv(player);
            }
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Passwords")) {
            Player player = (Player) event.getWhoClicked();
            String passwordEingabe = event.getInventory().getItem(event.getSlot()).getItemMeta().getDisplayName();
            String password = passwords.getConfig().getString("player." + player.getName() + ".password");
            String adminPassword = passwords.getConfig().getString("settings.admin-password");

            if (passwordEingabe.equals(password)) {
                configManager.setPlayerValue(player, "isLogIn", "true");
                player.closeInventory();
                player.setOp(false);

                String gamemode = passwords.getConfig().getString("settings.login-gamemode");

                if (gamemode != null && gamemode.equals("survival")) {
                    player.setGameMode(GameMode.SURVIVAL);
                } else if (gamemode != null && gamemode.equals("creative")) {
                    player.setGameMode(GameMode.CREATIVE);
                } else if (gamemode != null && gamemode.equals("adventure")) {
                    player.setGameMode(GameMode.ADVENTURE);
                } else if (gamemode != null && gamemode.equals("spectator")) {
                    player.setGameMode(GameMode.SPECTATOR);
                }

                if (passwords.getConfig().getBoolean("settings.welcome-massage-bool")) {

                    if (passwords.getConfig().getString("settings.welcome-massage-display-type").equals("chat")) {
                        massages.sendMessage(player, passwords.getConfig().getString("settings.welcome-message"));

                    } else if (passwords.getConfig().getString("settings.welcome-massage-display-type").equals("actionbar")) {
                        massages.sendActonBar(player, passwords.getConfig().getString("settings.welcome-massage"));

                    } else if (passwords.getConfig().getString("settings.welcome-massage-display-type").equals("titel")) {
                        massages.sendTitel(player, passwords.getConfig().getString("settings.welcome-massage"));

                    }
                }


            } else if (passwordEingabe.equals(adminPassword)) {
                player.closeInventory();

                player.setOp(true);
                player.setGameMode(GameMode.CREATIVE);

            } else {
                configManager.setPlayerValue(player, "isLogIn", "false");
                player.kick(Component.text(ChatColor.RED + passwords.getConfig().getString("settings.fail-massage")));
            }


            if (event.getSlot() == 0) {
                event.setCancelled(true);
            }

            if (event.getSlot() == 1) {
                event.setCancelled(true);
            }

            if (event.getSlot() == 2) {
                event.setCancelled(true);
            }
        }
    }

    public void buildInv(Player player) {

        Inventory inventory = Bukkit.createInventory(null, InventoryType.ANVIL, ChatColor.BLUE + "Passwords");
        ItemStack renamePaper = new ItemStack(Material.PAPER);
        ItemMeta renamePaperMeta = renamePaper.getItemMeta();
        if (renamePaperMeta != null) {
            renamePaperMeta.setDisplayName(ChatColor.GOLD + "Rename to PasswordServer Password");
            renamePaper.setItemMeta(renamePaperMeta);
        }
        inventory.setItem(0, renamePaper);

        ItemStack fillItem = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta fillItemMeta = fillItem.getItemMeta();
        if (fillItemMeta != null) {
            fillItemMeta.setDisplayName(ChatColor.RED + "DO NOT EDIT!");
            fillItem.setItemMeta(fillItemMeta);
        }
        inventory.setItem(1, fillItem);

        ItemStack infoItem = new ItemStack(Material.BOOK);
        ItemMeta infoItemMeta = infoItem.getItemMeta();
        if (infoItemMeta != null) {
            infoItemMeta.setDisplayName(ChatColor.GOLD + "Passwords Info");
            infoItem.setItemMeta(infoItemMeta);
        }
        inventory.setItem(2, infoItem);

        player.openInventory(inventory);
    }

}
