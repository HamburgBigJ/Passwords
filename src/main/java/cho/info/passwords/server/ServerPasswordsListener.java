package cho.info.passwords.server;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.ConfigManager;
import cho.info.passwords.utls.Messages;
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
                if (!ipAdress.equals(configManager.getPlayerValue(player, "playerIp"))) {
                    configManager.setPlayerValue(player, "playerIp", ipAdress);
                    configManager.setPlayerValue(player, "password", null);

                    int passwordLength = passwords.getConfig().getInt("settings.password-length");
                    for (int i = 0; i < passwordLength; i++) {
                        configManager.setPlayerValue(player, "char" + i, null);
                    }

                    openPasswordUI(player);
                    isIpLogin = false;
                } else if (ipAdress.equals(configManager.getPlayerValue(player, "playerIp"))) {
                    configManager.setPlayerValue(player, "isLogIn", true);
                    displayWelcomeMessage(player);
                    setGameMode(player);
                    player.closeInventory();
                    isIpLogin = true;
                }
            } else {
                isIpLogin = false;
                int passwordLength = passwords.getConfig().getInt("settings.password-length");
                for (int i = 0; i < passwordLength; i++) {
                    configManager.setPlayerValue(player, "char" + i, null);
                }
                configManager.setPlayerValue(player, "password", null);
                openPasswordUI(player);
            }
        }
    }

    public void openPasswordUI(Player player) {
        Inventory passwordInventory = passwords.getInventory();
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
        if (passwords.getConfig().getString("settings.check-type").equals("server")) {
            Player player = (Player) event.getWhoClicked();
            if (event.getView().getTitle().equals(passwords.getConfig().getString("settings.gui-name"))) {
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

                updateSlotAppearance(event, displayName);

                if (charSlot == (passwordLength - 1)) {
                    handlePasswordEntry(player, passwordLength);
                }
            }
        }
    }

    private void updateSlotAppearance(InventoryClickEvent event, String displayName) {
        int fixSlot = event.getSlot();
        int modelData = event.getCurrentItem().getItemMeta().getCustomModelData();

        ItemStack greenSlot = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta greenSlotMeta = greenSlot.getItemMeta();

        if (greenSlotMeta != null) {
            greenSlotMeta.setDisplayName(displayName);
            greenSlotMeta.setCustomModelData(modelData + 100);
            greenSlot.setItemMeta(greenSlotMeta);
        }

        event.getInventory().setItem(fixSlot, greenSlot);
    }

    private void handlePasswordEntry(Player player, int passwordLength) {
        String password = "";

        if (!isIpLogin) {
            for (int i = 0; i < passwordLength; i++) {
                password += configManager.getPlayerValue(player, "char" + i);
            }
            configManager.setPlayerValue(player, "password", password);
        } else {
            password = (String) configManager.getPlayerValue(player, "password");
        }

        if (password.equals(passwords.getConfig().getString("server.password"))) {
            configManager.setPlayerValue(player, "isLogIn", true);
            player.closeInventory();
            displayWelcomeMessage(player);
            setGameMode(player);
            setLoginIp(player);
        } else if (password.equals(passwords.getConfig().getString("settings.admin-password")) && passwords.getConfig().getBoolean("settings.admin-password-enabled")) {
            configManager.setPlayerValue(player, "isLogIn", true);
            player.closeInventory();
            player.setOp(passwords.getConfig().getBoolean("settings.is-admin-op"));
        } else {
            player.kick(Component.text(Objects.requireNonNull(passwords.getConfig().getString("settings.fail-message"))));
            configManager.setPlayerValue(player, "playerIp", "NULL");
        }
    }

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
            Boolean preventMovement = passwords.getConfig().getBoolean("settings.prevents-movement");

            if (preventMovement) {
                Boolean isLogIn = (Boolean) configManager.getPlayerValue(event.getPlayer(), "isLogIn");

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
        configManager.setPlayerValue(player, "playerIp", ipAdress);
    }

    private void displayWelcomeMessage(Player player) {
        if (passwords.getConfig().getBoolean("settings.welcome-message-enabled")) {
            Messages messages = new Messages();
            String messageType = passwords.getConfig().getString("settings.welcome-message-display-type");
            String message = passwords.getConfig().getString("settings.welcome-message");
            String secondMessage = passwords.getConfig().getString("settings.welcome-message-second");

            switch (messageType) {
                case "chat" -> messages.sendMessage(player, message);
                case "actionbar" -> messages.sendActonBar(player, message);
                case "title" -> messages.sendTitel(player, message, secondMessage);
                default -> passwords.getLogger().info(ChatColor.RED + "[Error] Invalid type for welcome message");
            }
        }
    }

    private void setGameMode(Player player) {
        if (passwords.getConfig().getBoolean("settings.login-gamemode-enabled")) {
            String gamemodeString = passwords.getConfig().getString("settings.login-gamemode");

            switch (gamemodeString) {
                case "survival" -> player.setGameMode(GameMode.SURVIVAL);
                case "creative" -> player.setGameMode(GameMode.CREATIVE);
                case "spectator" -> player.setGameMode(GameMode.SPECTATOR);
                case "adventure" -> player.setGameMode(GameMode.ADVENTURE);
                default -> passwords.getLogger().info(ChatColor.RED + "[Error] Invalid gamemode");
            }
        }
    }
}
