package info.cho.passwords.testing;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;

public class FakePluginManager implements PluginManager {
    private final Map<String, Boolean> pluginStates = new HashMap<>();

    @Override
    public boolean isPluginEnabled(String name) {
        return pluginStates.getOrDefault(name, false);
    }

    @Override
    public void setPluginEnabled(String name, boolean enabled) {
        pluginStates.put(name, enabled);
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        plugin.asJavaPlugin().setEnabled(false);
        plugin.onDisable();
    }

    @Override
    public void registerEvents(Listener listener, Plugin plugin) {
        // Events are not dispatched in the simplified environment.
    }
}
