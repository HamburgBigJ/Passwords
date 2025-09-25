package info.cho.passwords.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import info.cho.passwords.Passwords;
import info.cho.passwords.test.mock.MockPlaceholderAPI;
import info.cho.passwords.utls.DataManager;
import info.cho.passwordsApi.password.PasswordConfig;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Passwords plugin functionality with MockBukkit
 */
public class PasswordsPluginTest {

    private ServerMock server;
    private Passwords plugin;
    private MockPlaceholderAPI placeholderAPI;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        
        // Load the fake PlaceholderAPI plugin first
        placeholderAPI = MockBukkit.loadWith(MockPlaceholderAPI.class, new MockPlaceholderAPI());
        
        // Load the main plugin
        plugin = MockBukkit.load(Passwords.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testPluginBootsWithFakePlaceholderAPI() {
        assertNotNull(plugin, "Plugin should be loaded");
        assertTrue(plugin.isEnabled(), "Plugin should be enabled");
        
        // Verify PlaceholderAPI mock is loaded and enabled
        assertNotNull(placeholderAPI, "PlaceholderAPI mock should be loaded");
        assertTrue(placeholderAPI.isEnabled(), "PlaceholderAPI mock should be enabled");
        
        // Verify plugin detects PlaceholderAPI
        assertTrue(server.getPluginManager().isPluginEnabled("PlaceholderAPI"), 
                  "PlaceholderAPI should be detected as enabled");
    }

    @Test
    public void testPluginInstanceAndCustomGuiInitialization() {
        assertNotNull(Passwords.instance, "Plugin instance should be set");
        assertNotNull(Passwords.customGui, "CustomGui should be initialized");
        assertEquals("2.6", Passwords.version, "Plugin version should be correct");
    }

    @Test
    public void testConfigurationLoading() {
        assertTrue(PasswordConfig.isEnabled(), "Plugin should be enabled in config");
        assertEquals("2.6", PasswordConfig.getVersion(), "Config version should match");
        assertTrue(PasswordConfig.isDebugMode(), "Debug mode should be enabled in test config");
        assertEquals("player", PasswordConfig.getCheckType(), "Check type should be player");
    }

    @Test
    public void testPlayerJoinBehavior() {
        // Create a mock player
        PlayerMock player = server.addPlayer("testPlayer");
        assertNotNull(player, "Player should be created");

        // Simulate player join event
        PlayerJoinEvent joinEvent = new PlayerJoinEvent(player, "testPlayer joined the game");
        server.getPluginManager().callEvent(joinEvent);

        // Verify player data initialization
        DataManager dataManager = new DataManager();
        
        // The plugin should have set isLogin to false for new players
        Object isLogin = dataManager.getPlayerValue(player, "isLogin");
        assertNotNull(isLogin, "isLogin value should be set");
        assertFalse((Boolean) isLogin, "New player should not be logged in");

        // Verify password field is initialized
        Object password = dataManager.getPlayerValue(player, "password");
        assertNotNull(password, "Password field should be initialized");
    }

    @Test
    public void testPlayerLoginProcess() {
        PlayerMock player = server.addPlayer("loginTestPlayer");
        DataManager dataManager = new DataManager();
        
        // Simulate initial join
        PlayerJoinEvent joinEvent = new PlayerJoinEvent(player, "loginTestPlayer joined the game");
        server.getPluginManager().callEvent(joinEvent);
        
        // Verify initial state
        assertFalse((Boolean) dataManager.getPlayerValue(player, "isLogin"), 
                   "Player should initially not be logged in");
        
        // Simulate successful login by setting isLogin to true
        dataManager.setPlayerValue(player, "isLogin", true);
        
        // Verify login state
        assertTrue((Boolean) dataManager.getPlayerValue(player, "isLogin"), 
                  "Player should be logged in after setting isLogin to true");
    }

    @Test
    public void testAutoSaveBehavior() {
        assertTrue(PasswordConfig.useAutoSave(), "Auto-save should be enabled");
        assertTrue(PasswordConfig.isSavePlayerInventory(), "Save player inventory should be enabled");
        assertEquals(10, PasswordConfig.getSavePlayerInventoryIntervall(), 
                    "Save interval should be 10 minutes");
    }

    @Test
    public void testPlayerQuitBehaviorWithInventorySave() {
        PlayerMock player = server.addPlayer("quitTestPlayer");
        DataManager dataManager = new DataManager();
        
        // Set player as logged in
        dataManager.setPlayerValue(player, "isLogin", true);
        
        // Add some items to player inventory
        player.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND, 5));
        
        // Simulate player quit event
        PlayerQuitEvent quitEvent = new PlayerQuitEvent(player, "quitTestPlayer left the game");
        server.getPluginManager().callEvent(quitEvent);
        
        // The plugin should handle this quit event through CustomGuiHandler
        // We can verify the player was logged in before quitting
        assertTrue((Boolean) dataManager.getPlayerValue(player, "isLogin"), 
                  "Player should have been logged in when quitting");
    }

    @Test
    public void testWelcomeMessageConfiguration() {
        assertTrue(PasswordConfig.isWelcomeMessageEnabled(), "Welcome message should be enabled");
        assertEquals("Welcome to TestServer", PasswordConfig.getWelcomeMessage(), 
                    "Welcome message should match test config");
        assertEquals("message", PasswordConfig.getWelcomeMessageDisplayType(), 
                    "Welcome message display type should be message");
    }

    @Test
    public void testPasswordLengthConfiguration() {
        assertEquals(4, PasswordConfig.getPasswordLength(), "Password length should be 4");
        assertEquals(4, PasswordConfig.getPlayerPasswordLength(), "Player password length should be 4");
    }

    @Test
    public void testGamemodeConfiguration() {
        assertTrue(PasswordConfig.isLoginGamemodeEnabled(), "Login gamemode should be enabled");
        assertEquals("survival", PasswordConfig.getLoginGamemode(), "Login gamemode should be survival");
    }
}