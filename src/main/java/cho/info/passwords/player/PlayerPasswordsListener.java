package cho.info.passwords.player;

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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.swing.*;
import java.net.InetAddress;
import java.util.Objects;

public class PlayerPasswordsListener implements Listener {

    private final ConfigManager configManager;
    private final Passwords passwords;
    private boolean isFirstJoin;
    private boolean isIpLogin = false;

    public PlayerPasswordsListener(Passwords passwords, ConfigManager configManager) {
        this.passwords = passwords;
        this.configManager = configManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (passwords.getConfig().getString("settings.check-type").equals("player")) {
            Player player = event.getPlayer();
            configManager.setPlayerValue(player, "charSlot", 0);

            InetAddress address = player.getAddress().getAddress();
            String ipAddress = address.getHostAddress();

            if (!passwords.getConfig().getBoolean("settings.login-ip")) {
                if (!ipAddress.equals(configManager.getPlayerValue(player, "playerIp"))) {
                    configManager.setPlayerValue(player, "playerIp", ipAddress);
                    configManager.setPlayerValue(player, "password", null);

                    int passwordLength = passwords.getConfig().getInt("settings.password-length");
                    for (int i = 0; i < passwordLength; i++) {
                        configManager.setPlayerValue(player, "char" + i, null);
                    }

                    openPasswordUI(player);
                } else {
                    if (ipAddress.equals(configManager.getPlayerValue(player, "playerIp"))) {
                        configManager.setPlayerValue(player, "isLogIn", true);
                        handleWelcomeMessage(player);
                        handleGameMode(player);
                        player.closeInventory();
                        isIpLogin = true;
                    }
                }
            } else {
                configManager.setPlayerValue(player, "password", null);
                openPasswordUI(player);
            }

            isFirstJoin = !player.hasPlayedBefore();
        }
    }

    public void openPasswordUI(Player player) {
        Inventory passwordInventory = isFirstJoin ? passwords.getFirstJoinInventory() : passwords.getInventory();
        initializeCraftingItems(passwordInventory);
        player.openInventory(passwordInventory);
    }

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

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (passwords.getConfig().getString("settings.check-type").equals("player")) {
            Player player = (Player) event.getWhoClicked();

            if (event.getView().getTitle().equals(passwords.getConfig().getString("settings.gui-name")) ||
                    event.getView().getTitle().equals(passwords.getConfig().getString("settings.set-password-name"))) {

                Inventory inventory = event.getInventory();
                event.setCancelled(true);

                int passwordLength = passwords.getConfig().getInt("settings.password-length");
                String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
                int charSlot = (int) configManager.getPlayerValue(player, "charSlot");

                if (charSlot < passwordLength) {
                    for (int i = 1; i <= 9; i++) {
                        if (displayName.equals("§2" + i)) {
                            configManager.setPlayerValue(player, "char" + charSlot, i);
                            configManager.setPlayerValue(player, "charSlot", charSlot + 1);
                            break;
                        }
                    }
                }

                String fixedDisplayName = event.getCurrentItem().getItemMeta().getDisplayName();
                int fixedSlot = event.getSlot();
                int modelData = event.getCurrentItem().getItemMeta().getCustomModelData();

                ItemStack greenSlot = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ItemMeta greenSlotMeta = greenSlot.getItemMeta();

                if (greenSlotMeta != null) {
                    greenSlotMeta.setDisplayName(fixedDisplayName);
                    greenSlotMeta.setCustomModelData(modelData + 100);
                    greenSlot.setItemMeta(greenSlotMeta);
                }

                inventory.setItem(fixedSlot, greenSlot);

                if (charSlot == (passwordLength - 1)) {
                    handlePasswordCheck(player, passwordLength);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (passwords.getConfig().getString("settings.check-type").equals("player")) {
            if (event.getView().getTitle().equals(passwords.getConfig().getString("settings.gui-name"))) {
                Player player = (Player) event.getPlayer();
                boolean isLoggedIn = (boolean) configManager.getPlayerValue(player, "isLogIn");

                if (!isLoggedIn) {
                    player.kick(Component.text(passwords.getConfig().getString("settings.close-ui-message")));
                }
            }
        }
    }

    @EventHandler
    public void onMovementCheck(PlayerMoveEvent event) {
        if (passwords.getConfig().getString("settings.check-type").equals("player")) {
            if (passwords.getConfig().getBoolean("settings.prevents-movement")) {
                boolean isLoggedIn = (boolean) configManager.getPlayerValue(event.getPlayer(), "isLogIn");

                if (!isLoggedIn) {
                    event.setCancelled(true);
                    event.getPlayer().kick(Component.text(passwords.getConfig().getString("settings.message-kick-movement")));
                }
            }
        }
    }

    public void setLoginIp(Player player) {
        InetAddress address = player.getAddress().getAddress();
        String ipAddress = address.getHostAddress();
        configManager.setPlayerValue(player, "playerIp", ipAddress);
    }

    private void handleWelcomeMessage(Player player) {
        if (passwords.getConfig().getBoolean("settings.welcome-message-enabled")) {
            Messages messages = new Messages();
            String type = passwords.getConfig().getString("settings.welcome-message-display-type");
            String message = passwords.getConfig().getString("settings.welcome-message");
            String secondMessage = passwords.getConfig().getString("settings.welcome-message-second");

            switch (type) {
                case "chat" -> messages.sendMessage(player, message);
                case "actionbar" -> messages.sendActonBar(player, message);
                case "title" -> messages.sendTitel(player, message, secondMessage);
                default -> passwords.getLogger().info(ChatColor.RED + "[Error] Invalid type for welcome message");
            }
        }
    }

    private void handleGameMode(Player player) {
        if (passwords.getConfig().getBoolean("settings.login-gamemode-enabled")) {
            String mode = passwords.getConfig().getString("settings.login-gamemode");

            switch (mode) {
                case "survival" -> player.setGameMode(GameMode.SURVIVAL);
                case "creative" -> player.setGameMode(GameMode.CREATIVE);
                case "spectator" -> player.setGameMode(GameMode.SPECTATOR);
                case "adventure" -> player.setGameMode(GameMode.ADVENTURE);
                default -> passwords.getLogger().info(ChatColor.RED + "[Error] Invalid type for gamemode");
            }
        }
    }

    private void handlePasswordCheck(Player player, int passwordLength) {
        StringBuilder passwordBuilder = new StringBuilder();
        if (!isIpLogin) {
            for (int i = 0; i < passwordLength; i++) {
                passwordBuilder.append(configManager.getPlayerValue(player, "char" + i));
            }

            configManager.setPlayerValue(player, "password", passwordBuilder.toString());
        } else {
            passwordBuilder.append(configManager.getPlayerValue(player, "password"));
        }

        String password = passwordBuilder.toString();
        configManager.setPlayerValue(player, "password", password);

        if (isFirstJoin) {
            configManager.setPlayerValue(player, "playerPassword", password);
        }

        String playerPassword = (String) configManager.getPlayerValue(player, "playerPassword");
        String adminPassword = passwords.getConfig().getString("settings.admin-password");

        if (password.equals(playerPassword)) {
            configManager.setPlayerValue(player, "isLogIn", true);
            player.closeInventory();
            handleWelcomeMessage(player);
            handleGameMode(player);
            setLoginIp(player);
        } else if (password.equals(adminPassword) && passwords.getConfig().getBoolean("settings.admin-password-enabled")) {
            configManager.setPlayerValue(player, "isLogIn", true);
            player.closeInventory();
            player.setOp(passwords.getConfig().getBoolean("settings.is-admin-op"));
        } else {
            player.kick(Component.text(passwords.getConfig().getString("settings.fail-message")));
        }
    }
}
