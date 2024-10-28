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
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
            configManager.setPlayerValue(player, "isLogIn", false);
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

            // Konvertiere den Wert in Boolean
            String isLogInStr = (String) configManager.getPlayerValue(player, "isLogIn");
            boolean isLogIn = Boolean.parseBoolean(isLogInStr);

            if (!isLogIn) {
                buildInv(player);
            }
        }
    }


    // Event-Handler, um die Passwort-Eingabe im Anvil zu überprüfen
    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Passwords")) {
            Player player = (Player) event.getViewers().get(0);
            String password = passwords.getConfig().getString("player." + player.getName() + ".password");
            String adminPassword = passwords.getConfig().getString("settings.admin-password");

            if (event.getResult() != null && event.getResult().getItemMeta() != null) {
                String input = event.getResult().getItemMeta().getDisplayName();

                if (input.equals(password)) {
                    handleSuccessfulLogin(player, false);
                } else if (input.equals(adminPassword)) {
                    handleSuccessfulLogin(player, true);
                } else {
                    configManager.setPlayerValue(player, "isLogIn", false);
                    player.kick(Component.text(ChatColor.RED + passwords.getConfig().getString("settings.fail-massage")));
                }
            }
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Passwords")) {
            event.setCancelled(true); // Verhindert jegliche Klick-Interaktionen im Anvil
        }
    }

    public void buildInv(Player player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.ANVIL, ChatColor.BLUE + "Passwords");

        ItemStack renamePaper = new ItemStack(Material.PAPER);
        ItemMeta renamePaperMeta = renamePaper.getItemMeta();
        if (renamePaperMeta != null) {
            renamePaperMeta.setDisplayName(ChatColor.GOLD + "Rename to Password");
            renamePaper.setItemMeta(renamePaperMeta);
        }
        inventory.setItem(0, renamePaper);

        player.openInventory(inventory);
    }

    private void handleSuccessfulLogin(Player player, boolean isAdmin) {
        configManager.setPlayerValue(player, "isLogIn", true);
        player.closeInventory();

        // Admin bekommt OP-Rechte und Creative-Modus
        if (isAdmin) {
            player.setOp(true);
            player.setGameMode(GameMode.CREATIVE);
            return;
        }

        player.setOp(false);
        String gamemode = passwords.getConfig().getString("settings.login-gamemode");

        if (gamemode != null) {
            switch (gamemode.toLowerCase()) {
                case "survival" -> player.setGameMode(GameMode.SURVIVAL);
                case "creative" -> player.setGameMode(GameMode.CREATIVE);
                case "adventure" -> player.setGameMode(GameMode.ADVENTURE);
                case "spectator" -> player.setGameMode(GameMode.SPECTATOR);
            }
        }

        if (passwords.getConfig().getBoolean("settings.welcome-massage-bool")) {
            switch (passwords.getConfig().getString("settings.welcome-massage-display-type")) {
                case "chat" -> massages.sendMessage(player, passwords.getConfig().getString("settings.welcome-message"));
                case "actionbar" -> massages.sendActonBar(player, passwords.getConfig().getString("settings.welcome-massage"));
                case "titel" -> massages.sendTitel(player, passwords.getConfig().getString("settings.welcome-massage"));
            }
        }
    }
}
