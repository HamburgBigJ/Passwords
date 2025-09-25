package info.cho.passwords.test.mock;

import be.seeseemelk.mockbukkit.plugin.PluginMock;

/**
 * Mock PlaceholderAPI plugin for testing
 */
public class MockPlaceholderAPI extends PluginMock {

    public MockPlaceholderAPI() {
        super("PlaceholderAPI");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        // Mock the behavior of PlaceholderAPI
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}