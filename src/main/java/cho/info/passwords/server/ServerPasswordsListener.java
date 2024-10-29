package cho.info.passwords.server;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.ConfigManager;
import cho.info.passwords.utls.Massages;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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

        // Setze isLogIn auf false und initialisiere die Passwort-Felder
        configManager.setPlayerValue(player, "isLogIn", false);
        for (int i = 0; i < 4; i++) {
            configManager.setPlayerValue(player, "char" + i, null);
        }
        configManager.setPlayerValue(player, "charSlot", 0);
        configManager.setPlayerValue(player, "password", null);

        // Öffnet das benutzerdefinierte Passwort-UI
        openPasswordUI(player);
    }

    // Öffnet die benutzerdefinierte Passwort-Benutzeroberfläche mit blauem Titel
    public void openPasswordUI(Player player) {
        Inventory passwordInventory = Bukkit.createInventory(null, 9, Component.text(ChatColor.BLUE + "Passwords"));
        initializeCraftingItems(passwordInventory); // Füge Auswahl-Items hinzu
        player.openInventory(passwordInventory);
    }

    // Fügt nummerierte Gegenstände zur Passwort-UI hinzu
    public void initializeCraftingItems(Inventory inventory) {
        ItemStack selectItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        for (int i = 0; i < 9; i++) {
            ItemMeta itemMeta = selectItem.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(ChatColor.DARK_GREEN + "" + i + 1);
                selectItem.setItemMeta(itemMeta);
                inventory.setItem(i, selectItem);
            }
        }
    }

    // EventHandler für Klicks im Passwort-Inventar
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Überprüfe, ob der Titel des Passwort-UI entspricht
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Passwords")) {
            event.setCancelled(true); // Verhindert, dass Spieler die Items bewegen

            String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
            int charSlot = (int) configManager.getPlayerValue(player, "charSlot");

            // Füllt den Slot nur, wenn weniger als 4 Zeichen ausgewählt wurden
            if (charSlot < 4) {
                for (int i = 1; i <= 9; i++) {
                    if (displayName.equals(ChatColor.DARK_GREEN + "" + i)) {
                        configManager.setPlayerValue(player, "char" + charSlot, i);
                        configManager.setPlayerValue(player, "charSlot", charSlot + 1);
                        break;
                    }
                }
            }

            // Überprüft das Passwort, wenn 4 Zeichen ausgewählt wurden
            if (charSlot == 3) {
                String password = "";
                for (int i = 0; i < 4; i++) {
                    password += configManager.getPlayerValue(player, "char" + i);
                }

                configManager.setPlayerValue(player, "password", password);

                if (password.equals(passwords.getConfig().getString("server.password"))) {
                    configManager.setPlayerValue(player, "isLogIn", true);
                    player.closeInventory();

                    // Begrüßungsnachricht anzeigen
                    if (passwords.getConfig().getBoolean("settings.welcome-massage-bool")) {
                        Massages massages = new Massages();
                        String welcomeMessageType = passwords.getConfig().getString("settings.welcome-massage-display-type");
                        String welcomeMessage = passwords.getConfig().getString("settings.welcome-massage");

                        switch (welcomeMessageType) {
                            case "chat" -> massages.sendMessage(player, welcomeMessage);
                            case "titel", "actionbar" -> massages.sendActonBar(player, welcomeMessage);
                            default -> passwords.getLogger().info(ChatColor.RED + "[Error] Ungültiger Typ für Begrüßungsnachricht");
                        }
                    }
                } else {
                    player.kick(Component.text(ChatColor.RED + "" + ChatColor.BOLD + passwords.getConfig().getString("settings.fail-massage")));
                }
            }
        }
    }

    // Überprüft, ob das Inventar geschlossen wird, bevor das Passwort eingegeben wurde
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Passwords")) {
            Player player = (Player) event.getPlayer();
            Boolean isLogIn = (Boolean) configManager.getPlayerValue(player, "isLogIn");

            if (!isLogIn) {
                player.kick(Component.text(ChatColor.RED + "You need to enter the Password"));
            }
        }
    }
}
