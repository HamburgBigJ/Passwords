package org.bukkit.permissions;

import org.bukkit.plugin.java.JavaPlugin;

public class PermissionAttachment {
    private final JavaPlugin plugin;
    private final String permission;
    private boolean value;

    public PermissionAttachment(JavaPlugin plugin, String permission, boolean value) {
        this.plugin = plugin;
        this.permission = permission;
        this.value = value;
    }

    public String getPermission() {
        return permission;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
