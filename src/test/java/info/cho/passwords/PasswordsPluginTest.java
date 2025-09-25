package info.cho.passwords;

import com.github.seeseemelk.mockbukkit.MockBukkit;
import com.github.seeseemelk.mockbukkit.ServerMock;
import com.github.seeseemelk.mockbukkit.entity.PlayerMock;
import info.cho.passwords.utls.DataManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordsPluginTest {

    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void pluginDisablesWhenPlaceholderApiMissing() {
        Passwords plugin = MockBukkit.load(Passwords.class);

        assertFalse(plugin.isEnabled(), "The plugin should disable itself when PlaceholderAPI is missing.");
    }

    @Test
    void pluginEnablesWhenPlaceholderApiPresent() {
        JavaPlugin placeholder = MockBukkit.createMockPlugin("PlaceholderAPI");
        assertTrue(placeholder.isEnabled(), "The mock PlaceholderAPI plugin should be enabled for the test setup.");

        Passwords plugin = MockBukkit.load(Passwords.class);

        assertTrue(plugin.isEnabled(), "The plugin should remain enabled when PlaceholderAPI is available.");
    }

    @Test
    void autoSaveCopiesInventoryForLoggedInPlayers() throws Exception {
        MockBukkit.createMockPlugin("PlaceholderAPI");
        Passwords plugin = MockBukkit.load(Passwords.class);
        assertTrue(plugin.isEnabled(), "The plugin should be enabled when PlaceholderAPI is present.");

        plugin.getConfig().set("settings.save-player-inventory-intervall", 1);
        plugin.getConfig().set("settings.save-player-inventory", true);
        plugin.getConfig().set("settings.enable-auto-save", true);
        plugin.saveConfig();
        plugin.reloadConfig();

        Method savePlayerInventory = Passwords.class.getDeclaredMethod("savePlayerInventory");
        savePlayerInventory.setAccessible(true);
        savePlayerInventory.invoke(plugin);

        PlayerMock player = server.addPlayer("PasswordsTester");
        player.getInventory().setItem(0, new ItemStack(Material.DIAMOND));

        DataManager dataManager = new DataManager();
        dataManager.setPlayerValue(player, "isLogin", true);

        server.getScheduler().performTicks(1201);

        List<Object> storedInventory = dataManager.getListValue(player, "playerInventory");
        assertFalse(storedInventory.isEmpty(), "The stored inventory should contain entries for the player.");
        assertEquals(player.getInventory().getSize(), storedInventory.size(), "The stored inventory should mirror the player's inventory size.");

        ItemStack storedFirstItem = (ItemStack) storedInventory.get(0);
        assertEquals(Material.DIAMOND, storedFirstItem.getType(), "The first stored item should match the player's first inventory slot.");
    }
}
