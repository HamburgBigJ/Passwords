package info.cho.passwords.player;

import info.cho.passwords.Passwords;
import info.cho.passwords.config.Config;
import info.cho.passwords.util.DataManager;
import info.cho.passwords.util.Messages;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.log.Log;
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

@InjectableComponent
public class PlayerPasswordsListener implements Listener {

    private final Passwords passwords;
    private final DataManager dataManager;
    private final Config config;
    public boolean isIpLogin = false;
    public boolean isFistJoin = false;

    public PlayerPasswordsListener() {
        this.passwords = Passwords.instance;
        this.dataManager = Passwords.dataManager;
        this.config = Passwords.config;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (config.getCheckType().equals("player")) {
            Player player = event.getPlayer();


            dataManager.setPlayerValue(player, "charSlot", 0);

            InetAddress address = event.getPlayer().getAddress().getAddress();

            String ipAdress = address.getHostAddress();


            if (!config.isLoginIp()) {
                if (ipAdress != dataManager.getPlayerValue(player, "playerIp")) {
                    dataManager.setPlayerValue(player, "playerIp", ipAdress);

                    dataManager.setPlayerValue(player, "password", null);

                    int passwordLenth = config.getPasswordLength();
                    for (int i = 0; i < passwordLenth; i++) {
                        dataManager.setPlayerValue(player, "char" + i, null);
                    }

                    openPasswordUI(player);

                } else {

                    if (dataManager.getPlayerValue(player, "playerIp") == player.getAddress().getAddress()) {

                        dataManager.setPlayerValue(player, "isLogIn", true);

                        if (config.isWelcomeMessageEnabled()) {
                            Messages massages = new Messages();
                            String welcomeMessageType = config.getWelcomeMessageDisplayType();
                            String welcomeMessage = config.getWelcomeMessage();
                            String welcomeMessageSecond = config.getWelcomeMessageSecond();

                            switch (welcomeMessageType) {
                                case "chat" -> massages.sendMessage(player, welcomeMessage);
                                case "actionbar" -> massages.sendActionBar(player, welcomeMessage);
                                case "title" -> massages.sendTitle(player, welcomeMessage, welcomeMessageSecond);
                                default -> Log.info(ChatColor.RED + "[Error] Invalid type for welcome message");
                            }


                        }

                        // Gamemode
                        if (config.isLoginGamemodeEnabled()) {
                            String gamemodeString = config.getLoginGamemode();

                            switch (gamemodeString) {
                                case "survival" -> player.setGameMode(GameMode.SURVIVAL);
                                case "creative" -> player.setGameMode(GameMode.CREATIVE);
                                case "spectator" -> player.setGameMode(GameMode.SPECTATOR);
                                case "adventure" -> player.setGameMode(GameMode.ADVENTURE);
                                default -> Log.info(ChatColor.RED + "[Error] Invalid type for welcome message");
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
        // Inventory passwordInventory = Bukkit.createInventory(null, 9, Component.text(ChatColor.BLUE + "Passwords")); Chest
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
                itemMeta.setDisplayName("§2" + (i + 1) );
                itemMeta.setCustomModelData(2700 + i);
                selectItem.setItemMeta(itemMeta);
                inventory.setItem(i, selectItem);
            }
        }

    }

    // Event handler for clicks in the password inventory
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (config.getCheckType().equals("player")) {
            Player player = (Player) event.getWhoClicked();

            // Checks if the title matches the password UI
            if (event.getView().getTitle().equals(config.getGuiName()) || event.getView().getTitle().equals(config.getSetPasswordName())) {
                Inventory inventory = event.getInventory();
                event.setCancelled(true); // Prevents players from moving items

                int passwordLenth = config.getPasswordLength();

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



                    String adminPassword = (String) config.getAdminPassword();



                    if (password.equals(playerPassword)) {
                        dataManager.setPlayerValue(player, "isLogIn", true);
                        player.closeInventory();

                        // Display welcome message
                        if (config.isWelcomeMessageEnabled()) {
                            Messages massages = new Messages();
                            String welcomeMessageType = config.getWelcomeMessageDisplayType();
                            String welcomeMessage = config.getWelcomeMessage();
                            String welcomeMessageSecond = config.getWelcomeMessageSecond();

                            switch (welcomeMessageType) {
                                case "chat" -> massages.sendMessage(player, welcomeMessage);
                                case "actionbar" -> massages.sendActionBar(player, welcomeMessage);
                                case "title" -> massages.sendTitle(player, welcomeMessage, welcomeMessageSecond);
                                default -> Log.info(ChatColor.RED + "[Error] Invalid type for welcome message");
                            }


                        }

                        // Gamemode
                        if (config.isLoginGamemodeEnabled()) {
                            String gamemodeString = config.getLoginGamemode();

                            switch (gamemodeString) {
                                case "survival" -> player.setGameMode(GameMode.SURVIVAL);
                                case "creative" -> player.setGameMode(GameMode.CREATIVE);
                                case "spectator" -> player.setGameMode(GameMode.SPECTATOR);
                                case "adventure" -> player.setGameMode(GameMode.ADVENTURE);
                                default -> Log.info(ChatColor.RED + "[Error] Invalid type for welcome message");
                            }
                        }

                        setLoginIp(player);

                    } else if (password.equals(adminPassword) && config.isAdminPasswordEnabled()) {
                        dataManager.setPlayerValue(player, "isLogIn", true);
                        player.closeInventory();

                        player.setOp(config.isAdminOp());
                    } else {
                        player.kickPlayer(config.getFailMessage());
                    }
                }
            }

        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (config.getCheckType().equals("player")) {
            if (event.getView().getTitle().equals(config.getGuiName())) {
                Player player = (Player) event.getPlayer();
                Boolean isLogIn = (Boolean) dataManager.getPlayerValue(player, "isLogIn");

                if (isLogIn == null || !isLogIn) {
                    player.kickPlayer(config.getCloseUiMessage());
                }
            }
        }
    }

    @EventHandler
    public void onMovementCheck(PlayerMoveEvent event) {

        if (config.getCheckType().equals("player")) {

            Boolean proventMovment = config.isPreventsMovement();

            if (proventMovment) {

                Boolean isLogIn = (Boolean) dataManager.getPlayerValue(event.getPlayer(), "isLogIn");

                if (!isLogIn) {
                    event.setCancelled(true);

                    event.getPlayer().kickPlayer(config.getMessageKickMovement());
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
