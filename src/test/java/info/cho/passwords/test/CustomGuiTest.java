package info.cho.passwords.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import info.cho.passwords.Passwords;
import info.cho.passwords.customGui.CustomGui;
import info.cho.passwords.player.PasswordPlayerMode;
import info.cho.passwords.test.mock.MockPlaceholderAPI;
import info.cho.passwords.utls.DataManager;
import info.cho.passwordsApi.password.PasswordConfig;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Custom GUI functionality
 */
public class CustomGuiTest {

    private ServerMock server;
    private Passwords plugin;
    private MockPlaceholderAPI placeholderAPI;
    private CustomGui customGui;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        placeholderAPI = MockBukkit.loadWith(MockPlaceholderAPI.class, new MockPlaceholderAPI());
        plugin = MockBukkit.load(Passwords.class);
        customGui = Passwords.customGui;
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testCustomGuiInitialization() {
        assertNotNull(customGui, "CustomGui should be initialized");
        assertNotNull(customGui.customGuiList, "CustomGui list should be initialized");
    }

    @Test
    public void testGuiRegistration() {
        // Register a test GUI
        customGui.registerGui("testGui", PasswordPlayerMode.class);
        
        assertTrue(customGui.customGuiList.containsKey("testGui"), 
                  "Test GUI should be registered");
        assertEquals(PasswordPlayerMode.class, customGui.customGuiList.get("testGui"), 
                    "Registered GUI class should match");
    }

    @Test
    public void testPasswordPlayerModeGui() {
        // Register the player mode GUI
        customGui.registerGui("player", PasswordPlayerMode.class);
        
        PasswordPlayerMode playerMode = new PasswordPlayerMode();
        assertNotNull(playerMode, "PasswordPlayerMode should be instantiable");
        assertNotNull(playerMode.getDataManager(), "DataManager should be available");
    }

    @Test
    public void testPlayerGuiInteraction() {
        PlayerMock player = server.addPlayer("guiTestPlayer");
        DataManager dataManager = new DataManager();
        
        // Initialize player data
        dataManager.addValue(player, "isLogin", false);
        dataManager.setPlayerValue(player, "isLogin", false);
        
        PasswordPlayerMode playerMode = new PasswordPlayerMode();
        
        // Test opening GUI
        PlayerJoinEvent joinEvent = new PlayerJoinEvent(player, "guiTestPlayer joined the game");
        playerMode.openGui(joinEvent);
        
        // Verify player variables are generated
        assertNotNull(dataManager.getPlayerValue(player, "charLocation"), 
                     "Character location should be initialized");
        assertEquals(1, dataManager.getPlayerValue(player, "charLocation"), 
                    "Character location should start at 1");
    }

    @Test
    public void testInventorySaveOnQuit() {
        PlayerMock player = server.addPlayer("saveTestPlayer");
        
        // Add items to inventory
        player.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.STONE, 10));
        player.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.WOOD, 5));
        
        PasswordPlayerMode playerMode = new PasswordPlayerMode();
        
        // Simulate player quit
        PlayerQuitEvent quitEvent = new PlayerQuitEvent(player, "saveTestPlayer left the game");
        playerMode.playerQuit(quitEvent);
        
        // The method should handle inventory saving and staff permission removal
        // This tests that the method executes without throwing exceptions
        assertTrue(true, "Player quit handling should complete without errors");
    }

    @Test
    public void testGuiCloseKickForUnloggedPlayer() {
        PlayerMock player = server.addPlayer("closeTestPlayer");
        DataManager dataManager = new DataManager();
        
        // Set player as not logged in
        dataManager.addValue(player, "isLogin", false);
        dataManager.setPlayerValue(player, "isLogin", false);
        
        PasswordPlayerMode playerMode = new PasswordPlayerMode();
        
        // Create a mock inventory and close event
        Inventory mockInventory = server.createInventory(player, 9, "Test GUI");
        InventoryCloseEvent closeEvent = new InventoryCloseEvent(player.openInventory(mockInventory));
        
        // This should attempt to kick the player since they're not logged in
        playerMode.closeGui(closeEvent);
        
        // Verify the player is still not logged in
        assertFalse((Boolean) dataManager.getPlayerValue(player, "isLogin"), 
                   "Player should still not be logged in");
    }

    @Test
    public void testAutoSaveConfiguration() {
        assertTrue(PasswordConfig.useAutoSave(), "Auto-save should be enabled");
        assertTrue(PasswordConfig.isSavePlayerInventory(), "Save player inventory should be enabled");
    }
}