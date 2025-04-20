package cho.info.passwords.player;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.DataManager;
import cho.info.passwords.utls.Messages;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.net.InetAddress;
import java.util.Objects;

public class PlayerPasswordsListener implements Listener {

    public DataManager dataManager;
    public Passwords passwords;

    public boolean isFistJoin;
    public boolean isIpLogin = false;

    public PlayerPasswordsListener() {
        this.passwords = Passwords.instance;
        this.dataManager = Passwords.dataManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (event.getPlayer().isDead()) {
            event.getPlayer().spigot().respawn();
        }

        if (passwords.getConfig().getString("settings.check-type").equals("player")) {
            Player player = event.getPlayer();


            dataManager.setPlayerValue(player, "charSlot", 0);

            InetAddress address = event.getPlayer().getAddress().getAddress();

            String ipAdress = address.getHostAddress();


            if (!passwords.getConfig().getBoolean("settings.login-ip")) {
                if (ipAdress != dataManager.getPlayerValue(player, "playerIp")) {
                    dataManager.setPlayerValue(player, "playerIp", ipAdress);

                    dataManager.setPlayerValue(player, "password", null);

                    int passwordLenth = passwords.getConfig().getInt("settings.password-length");
                    for (int i = 0; i < passwordLenth; i++) {
                        dataManager.setPlayerValue(player, "char" + i, null);
                    }

                    openPasswordUI(player);

                } else {

                    if (dataManager.getPlayerValue(player, "playerIp") == player.getAddress().getAddress()) {

                        dataManager.setPlayerValue(player, "isLogIn", true);

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

                        player.closeInventory();

                        isIpLogin = true;
                    }

                }
            } else {
                dataManager.setPlayerValue(player, "password", null);
                openPasswordUI(player);
            }


            // First join detection
            if (!event.getPlayer().hasPlayedBefore()) {
                isFistJoin = true;
            } else isFistJoin = false;

        }

    }

    // Opens the custom password user interface with a blue title
    public void openPasswordUI(Player player) {
        if (isFistJoin) {
            Inventory passwordInventory = passwords.getFirstJoinInventory();
            initializeCraftingItems(passwordInventory); // Adds selection items
            player.openInventory(passwordInventory);
        }else {

            Inventory passwordInventory = passwords.getInventory();
            initializeCraftingItems(passwordInventory); // Adds selection items
            player.openInventory(passwordInventory);
        }

    }

    // Adds numbered items to the password UI
    public void initializeCraftingItems(Inventory inventory) {
        ItemStack selectItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        for (int i = 0; i < 9; i++) {
            ItemMeta itemMeta = selectItem.getItemMeta();
            if (itemMeta != null) {
                itemMeta.displayName(Component.text("§2" + (i + 1) ));
                selectItem.setItemMeta(itemMeta);
                inventory.setItem(i, selectItem);
            }
        }

    }

    // Event handler for clicks in the password inventory
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (passwords.getConfig().getString("settings.check-type").equals("player")) {
            Player player = (Player) event.getWhoClicked();

            // Checks if the title matches the password UI
            if (event.getView().getTitle().equals(passwords.getConfig().getString("settings.gui-name")) || event.getView().getTitle().equals(passwords.getConfig().getString("settings.set-password-name"))) {
                Inventory inventory = event.getInventory();
                event.setCancelled(true); // Prevents players from moving items

                int passwordLenth = passwords.getConfig().getInt("settings.password-length");

                String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
                int charSlot = (int) dataManager.getPlayerValue(player, "charSlot");

                // Fills the slot only if less than 4 characters have been selected
                if (charSlot < passwordLenth) {
                    for (int i = 1; i <= 9; i++) {
                        if (displayName.equals("§2" + i)) {
                            dataManager.setPlayerValue(player, "char" + charSlot, i);
                            dataManager.setPlayerValue(player, "charSlot", charSlot + 1);
                            break;
                        }
                    }
                }

                // UI Back
                String fixDisplayName = String.valueOf(event.getCurrentItem().getItemMeta().displayName());
                int fixSlot = event.getSlot();

                ItemStack greenSlot = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ItemMeta greenSlotMeta = greenSlot.getItemMeta();

                if (greenSlotMeta != null) {
                    greenSlotMeta.displayName(Component.text(fixDisplayName));
                    greenSlot.setItemMeta(greenSlotMeta);
                }

                inventory.setItem(fixSlot, greenSlot);

                // Checks the password when 4 characters have been selected
                if (charSlot == (passwordLenth - 1)) {
                    String password = "";
                    if (!isIpLogin) {

                        for (int i = 0; i < passwordLenth; i++) {
                            password += dataManager.getPlayerValue(player, "char" + i);
                        }

                        dataManager.setPlayerValue(player, "password", password);
                    }else {
                        password = (String) dataManager.getPlayerValue(player, "password");

                    }



                    dataManager.setPlayerValue(player, "password", password);

                    if (isFistJoin) {
                        dataManager.setPlayerValue(player, "playerPassword", password);
                    }

                    String playerPassword = (String) dataManager.getPlayerValue(player, "playerPassword");



                    String adminPassword = (String) passwords.getConfig().getString("settings.admin-password");



                    if (password.equals(playerPassword)) {
                        dataManager.setPlayerValue(player, "isLogIn", true);
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

                        setLoginIp(player);

                    } else if (password.equals(adminPassword) && passwords.getConfig().getBoolean("settings.admin-password-enabled")) {
                        dataManager.setPlayerValue(player, "isLogIn", true);
                        player.closeInventory();

                        player.setOp(passwords.getConfig().getBoolean("settings.is-admin-op"));
                    } else {
                        player.kick(Component.text(Objects.requireNonNull(passwords.getConfig().getString("settings.fail-message"))));
                    }
                }
            }

        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (passwords.getConfig().getString("settings.check-type").equals("player")) {
            if (event.getView().getTitle().equals(passwords.getConfig().getString("settings.gui-name"))) {
                Player player = (Player) event.getPlayer();
                Boolean isLogIn = (Boolean) dataManager.getPlayerValue(player, "isLogIn");

                if (isLogIn == null || !isLogIn) {
                    player.kick(Component.text(Objects.requireNonNull(passwords.getConfig().getString("settings.close-ui-message"))));
                }
            }
        }
    }

    @EventHandler
    public void onMovementCheck(PlayerMoveEvent event) {

        if (passwords.getConfig().getString("settings.check-type").equals("player")) {

            Boolean proventMovment = passwords.getConfig().getBoolean("settings.prevents-movement");

            if (proventMovment) {

                Boolean isLogIn = (Boolean) dataManager.getPlayerValue(event.getPlayer(), "isLogIn");

                if (!isLogIn) {
                    event.setCancelled(true);

                    event.getPlayer().kick(Component.text(Objects.requireNonNull(passwords.getConfig().getString("settings.message-kick-movement"))));
                }

            }
        }


    }

    public void setLoginIp(Player player) {
        InetAddress address = player.getAddress().getAddress();
        String ipAdress = address.getHostAddress();

        dataManager.setPlayerValue(player, "playerIp", ipAdress);
    }
}