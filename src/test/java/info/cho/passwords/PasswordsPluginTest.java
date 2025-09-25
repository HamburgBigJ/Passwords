package info.cho.passwords;

import info.cho.passwords.customGui.CustomGui;
import info.cho.passwords.testing.FakePlayer;
import info.cho.passwords.testing.FakeServer;
import info.cho.passwords.testing.TestEnvironment;
import info.cho.passwords.utls.DataManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordsPluginTest {

    private FakeServer server;

    @BeforeEach
    void setUp() {
        server = TestEnvironment.createServer();
    }

    @AfterEach
    void tearDown() {
        server.clearPlayers();
    }

    @Test
    void pluginDisablesWhenPlaceholderApiMissing() {
        TestablePasswords plugin = createPlugin();
        configureDefaults(plugin.getConfig(), false);
        plugin.setEnabled(true);

        plugin.onEnable();

        assertFalse(plugin.isEnabled(), "The plugin should disable itself when PlaceholderAPI is missing.");
    }

    @Test
    void pluginEnablesWhenPlaceholderApiPresent() {
        TestablePasswords plugin = createPlugin();
        configureDefaults(plugin.getConfig(), true);
        plugin.setEnabled(true);

        plugin.onEnable();

        assertTrue(plugin.isEnabled(), "The plugin should remain enabled when PlaceholderAPI is available.");
    }

    @Test
    void autoSaveCopiesInventoryForLoggedInPlayers() throws Exception {
        TestablePasswords plugin = createPlugin();
        configureDefaults(plugin.getConfig(), true);
        plugin.getConfig().set("settings.save-player-inventory", true);
        plugin.getConfig().set("settings.enable-auto-save", true);
        plugin.getConfig().set("settings.save-player-inventory-intervall", 1);
        plugin.saveConfig();
        plugin.reloadConfig();
        plugin.setEnabled(true);

        plugin.onEnable();

        Method savePlayerInventory = Passwords.class.getDeclaredMethod("savePlayerInventory");
        savePlayerInventory.setAccessible(true);
        savePlayerInventory.invoke(plugin);

        FakePlayer player = new FakePlayer("PasswordsTester", 36);
        player.getInventory().setItem(0, new ItemStack(Material.DIAMOND));
        server.addPlayer(player);

        DataManager dataManager = new DataManager();
        dataManager.setPlayerValue(player, "isLogin", true);

        server.getScheduler().performTicks(1200);

        DataManager verificationManager = new DataManager();
        List<Object> storedInventory = verificationManager.getListValue(player, "playerInventory");
        assertFalse(storedInventory.isEmpty(), "The stored inventory should contain entries for the player.");
        assertEquals(player.getInventory().getSize(), storedInventory.size(), "The stored inventory should mirror the player's inventory size.");

        ItemStack storedFirstItem = (ItemStack) storedInventory.get(0);
        assertEquals(Material.DIAMOND, storedFirstItem.getType(), "The first stored item should match the player's first inventory slot.");
    }

    private TestablePasswords createPlugin() {
        TestablePasswords plugin = new TestablePasswords();
        plugin.onLoad();
        return plugin;
    }

    private void configureDefaults(FileConfiguration config, boolean placeholderEnabled) {
        server.getPluginManager().setPluginEnabled("PlaceholderAPI", placeholderEnabled);
        config.set("enable", true);
        config.set("version", Passwords.version);
        config.set("debug", false);
        config.set("settings.enable-auto-save", false);
        config.set("settings.save-player-inventory", false);
        config.set("settings.save-player-inventory-intervall", 10);
    }

    private static class TestablePasswords extends Passwords {
        @Override
        protected void createGuiHandler(CustomGui customGui) {
            // Skip heavy GUI registration in tests
        }

        @Override
        protected void createDiscordHook() {
            // Skip Discord integration during tests
        }

        @Override
        protected void registerCommands() {
            // Skip command registration
        }

        @Override
        protected void registerDefaultGuis(CustomGui customGui) {
            // Skip GUI registration that relies on external libraries
        }
    }
}
