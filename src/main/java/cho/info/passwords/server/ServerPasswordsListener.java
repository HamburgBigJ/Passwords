package cho.info.passwords.server;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.ConfigManager;
import cho.info.passwords.utls.Messages;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.net.InetAddress;
import java.util.Objects;

public class ServerPasswordsListener implements Listener {

    private final Passwords passwords;
    private final ConfigManager configManager;

    public boolean isIpLogin = false;

    public ServerPasswordsListener(Passwords passwords, ConfigManager configManager) {
        this.passwords = passwords;
        this.configManager = configManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (passwords.getConfig().getString("settings.check-type").equals("server")) {
            Player player = event.getPlayer();

            configManager.setPlayerValue(player, "charSlot", 0);


            InetAddress address = event.getPlayer().getAddress().getAddress();

            String ipAdress = address.getHostAddress();

            if (passwords.getConfig().getBoolean("settings.login-ip")) {
                if (ipAdress != configManager.getPlayerValue(player, "playerIp")) {
                    configManager.setPlayerValue(player, "playerIp", ipAdress);

                    configManager.setPlayerValue(player, "password", null);

                    int passwordLenth = passwords.getConfig().getInt("settings.password-length");
                    for (int i = 0; i < passwordLenth; i++) {
                        configManager.setPlayerValue(player, "char" + i, null);
                    }

                    openPasswordUI(player);

                    isIpLogin = false;

                } else {

                    configManager.setPlayerValue(player, "isLogIn", true);

                    if (passwords.getConfig().getBoolean("settings.welcome-message-enabled")) {
                            Messages massages = new Messages();
                            String welcomeMessageType = passwords.getConfig().getString("settings.welcome-message-display-type");
                            String welcomeMessage = passwords.getConfig().getString("settings.welcome-message");
                            String welcomeMessageSecond = passwords.getConfig().getString("settings.welcome-message-second");

                            switch (welcomeMessageType) {
                                case "chat" -> massages.sendMessage(player, welcomeMessage);
                                case "actionbar" -> massages.sendActonBar(player, welcomeMessage);
                                case "title" -> massages.sendTitel(player, welcomeMessage, welcomeMessageSecond);
                                default -> passwords.getLogger().info(ChatColor.RED + "[Error] Invalid type for welcome message");
                            }


                        }

                        // Gamemode
                        if (passwords.getConfig().getBoolean("settings.login-gamemode-enabled")) {
                            String gamemodeString = passwords.getConfig().getString("settings.login-gamemode");

                            switch (gamemodeString) {
                                case "survival" -> player.setGameMode(GameMode.SURVIVAL);
                                case "creative" -> player.setGameMode(GameMode.CREATIVE);
                                case "spectator" -> player.setGameMode(GameMode.SPECTATOR);
                                case "adventure" -> player.setGameMode(GameMode.ADVENTURE);
                                default -> passwords.getLogger().info(ChatColor.RED + "[Error] Invalid type for welcome message");
                            }
                        }

                        // Ip Login
                        isIpLogin = true;

                }
            } else {
                isIpLogin = false;
                int passwordLenth = passwords.getConfig().getInt("settings.password-length");
                for (int i = 0; i < passwordLenth; i++) {
                    configManager.setPlayerValue(player, "char" + i, null);
                }
                configManager.setPlayerValue(player, "password", null);
                openPasswordUI(player);
            }





        }
    }

    // Opens the custom password user interface with a blue title
    public void openPasswordUI(Player player) {
        // Inventory passwordInventory = Bukkit.createInventory(null, 9, Component.text(ChatColor.BLUE + "Passwords")); Chest
        Inventory passwordInventory = passwords.getInventory();
        initializeCraftingItems(passwordInventory); // Adds selection items
        player.openInventory(passwordInventory);
    }

    // Adds numbered items to the password UI
    public void initializeCraftingItems(Inventory inventory) {
        ItemStack selectItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        for (int i = 0; i < 9; i++) {
            ItemMeta itemMeta = selectItem.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName("§2" + (i + 1));
                itemMeta.setCustomModelData(2700 + i);
                selectItem.setItemMeta(itemMeta);
                inventory.setItem(i, selectItem);
            }
        }
    }

    // Event handler for clicks in the password inventory
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (passwords.getConfig().getString("settings.check-type").equals("server")) {
            Player player = (Player) event.getWhoClicked();

            // Checks if the title matches the password UI
            if (event.getView().getTitle().equals(passwords.getConfig().getString("settings.gui-name"))) {
                Inventory inventory = event.getInventory();
                event.setCancelled(true); // Prevents players from moving items

                int passwordLenth = passwords.getConfig().getInt("settings.password-length");

                String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
                int charSlot = (int) configManager.getPlayerValue(player, "charSlot");

                // Fills the slot only if less than 4 characters have been selected
                if (charSlot < passwordLenth) {
                    for (int i = 1; i <= 9; i++) {
                        if (displayName.equals("§2" + i)) {
                            configManager.setPlayerValue(player, "char" + charSlot, i);
                            configManager.setPlayerValue(player, "charSlot", charSlot + 1);
                            break;
                        }
                    }
                }

                // UI Back
                String fixDisplayName = event.getCurrentItem().getItemMeta().getDisplayName();
                int fixSlot = event.getSlot();
                int modleData = event.getCurrentItem().getItemMeta().getCustomModelData();

                ItemStack greenSlot = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ItemMeta greenSlotMeta = greenSlot.getItemMeta();

                if (greenSlotMeta != null) {
                    greenSlotMeta.setDisplayName(fixDisplayName);
                    greenSlotMeta.setCustomModelData(modleData + 100);
                    greenSlot.setItemMeta(greenSlotMeta);
                }

                inventory.setItem(fixSlot, greenSlot);

                // Checks the password when 4 characters have been selected


                if (charSlot == (passwordLenth - 1)) {
                    String password = "";
                    if (!isIpLogin) {

                        for (int i = 0; i < passwordLenth; i++) {
                            password += configManager.getPlayerValue(player, "char" + i);
                        }

                        configManager.setPlayerValue(player, "password", password);
                    }else {
                        password = (String) configManager.getPlayerValue(player, "password");

                    }


                    if (password.equals(passwords.getConfig().getString("server.password"))) {
                        configManager.setPlayerValue(player, "isLogIn", true);
                        player.closeInventory();

                        // Display welcome message
                        if (passwords.getConfig().getBoolean("settings.welcome-message-enabled")) {
                            Messages massages = new Messages();
                            String welcomeMessageType = passwords.getConfig().getString("settings.welcome-message-display-type");
                            String welcomeMessage = passwords.getConfig().getString("settings.welcome-message");
                            String welcomeMessageSecond = passwords.getConfig().getString("settings.welcome-message-second");

                            switch (welcomeMessageType) {
                                case "chat" -> massages.sendMessage(player, welcomeMessage);
                                case "actionbar" -> massages.sendActonBar(player, welcomeMessage);
                                case "title" -> massages.sendTitel(player, welcomeMessage, welcomeMessageSecond);
                                default -> passwords.getLogger().info(ChatColor.RED + "[Error] Invalid type for welcome message");
                            }


                        }

                        // Gamemode
                        if (passwords.getConfig().getBoolean("settings.login-gamemode-enabled")) {
                            String gamemodeString = passwords.getConfig().getString("settings.login-gamemode");

                            switch (gamemodeString) {
                                case "survival" -> player.setGameMode(GameMode.SURVIVAL);
                                case "creative" -> player.setGameMode(GameMode.CREATIVE);
                                case "spectator" -> player.setGameMode(GameMode.SPECTATOR);
                                case "adventure" -> player.setGameMode(GameMode.ADVENTURE);
                                default -> passwords.getLogger().info(ChatColor.RED + "[Error] Invalid type for welcome message");
                            }
                        }
                        
                    } else if (password.equals(passwords.getConfig().getString("settings.admin-password")) && passwords.getConfig().getBoolean("settings.admin-password-enabled")) {
                        configManager.setPlayerValue(player, "isLogIn", true);
                        player.closeInventory();

                        player.setOp(passwords.getConfig().getBoolean("settings.is-admin-op"));
                    } else {
                        player.kick(Component.text(Objects.requireNonNull(passwords.getConfig().getString("settings.fail-message"))));
                        configManager.setPlayerValue(player, "playerIp", "NULL");
                    }
                }
            }

        }

    }

    // Checks if the inventory is closed before the password is entered
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (passwords.getConfig().getString("settings.check-type").equals("server")) {
            if (event.getView().getTitle().equals(passwords.getConfig().getString("settings.gui-name"))) {
                Player player = (Player) event.getPlayer();
                Boolean isLogIn = (Boolean) configManager.getPlayerValue(player, "isLogIn");

                if (!isLogIn) {
                    player.kick(Component.text(Objects.requireNonNull(passwords.getConfig().getString("settings.close-ui-message"))));
                }
            }
        }

    }

    @EventHandler
    public void onMovementCheck(PlayerMoveEvent event) {

        if (passwords.getConfig().getString("settings.check-type").equals("server")) {

            Boolean proventMovment = passwords.getConfig().getBoolean("settings.provents-movement");

            if (proventMovment) {

                Boolean isLogIn = (Boolean) configManager.getPlayerValue(event.getPlayer(), "isLogIn");

                if (!isLogIn) {
                    event.setCancelled(true);

                    event.getPlayer().kick(Component.text(Objects.requireNonNull(passwords.getConfig().getString("settings.message-kick-movment"))));
                }

            }
        }

    }
}
