package info.cho.passwords.test;

import info.cho.passwords.Passwords;
import info.cho.passwords.customGui.CustomGui;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple tests that verify basic functionality without external dependencies
 */
public class SimplePasswordsTest {

    @Test
    public void testPasswordsClassExists() {
        assertNotNull(Passwords.class, "Passwords class should exist");
    }

    @Test
    public void testVersionConstant() {
        assertEquals("2.6", Passwords.version, "Version should be 2.6");
    }

    @Test
    public void testCustomGuiClassExists() {
        assertNotNull(CustomGui.class, "CustomGui class should exist");
    }

    @Test
    public void testCustomGuiInstantiation() {
        CustomGui gui = new CustomGui();
        assertNotNull(gui, "CustomGui should be instantiable");
        assertNotNull(gui.customGuiList, "CustomGui list should be initialized");
    }

    @Test
    public void testCustomGuiRegistration() {
        CustomGui gui = new CustomGui();
        
        // Test registering a gui
        gui.registerGui("test", String.class);
        assertTrue(gui.customGuiList.containsKey("test"), "GUI should be registered");
        assertEquals(String.class, gui.customGuiList.get("test"), "Registered class should match");
        
        // Test registering same gui again (should not duplicate)
        gui.registerGui("test", String.class);
        assertEquals(1, gui.customGuiList.size(), "Should not register duplicate GUI");
    }

    @Test
    public void testConfigurationClassesExist() {
        // Test that configuration related classes exist
        assertDoesNotThrow(() -> {
            Class.forName("info.cho.passwordsApi.password.PasswordConfig");
        }, "PasswordConfig class should exist");
        
        assertDoesNotThrow(() -> {
            Class.forName("info.cho.passwords.utls.DataManager");
        }, "DataManager class should exist");
    }
}