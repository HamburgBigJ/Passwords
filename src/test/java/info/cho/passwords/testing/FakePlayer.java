package info.cho.passwords.testing;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.SimplePlayerInventory;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class FakePlayer implements Player {
    private final UUID uniqueId = UUID.randomUUID();
    private final String name;
    private PlayerInventory inventory;
    private final Spigot spigot = new Spigot() {
        @Override
        public void respawn() {
        }
    };
    private boolean invulnerable;

    public FakePlayer(String name, int inventorySize) {
        this.name = name;
        this.inventory = new SimplePlayerInventory(inventorySize, Component.text("Inventory"), new java.util.ArrayList<>(java.util.Collections.nCopies(inventorySize, null)));
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PlayerInventory getInventory() {
        return inventory;
    }

    @Override
    public void setInventory(PlayerInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void openInventory(Inventory inventory) {
        if (inventory instanceof PlayerInventory playerInventory) {
            this.inventory = playerInventory;
        }
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public Spigot spigot() {
        return spigot;
    }

    @Override
    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    @Override
    public void kick(Component message) {
    }

    @Override
    public void sendActionBar(Component component) {
    }

    @Override
    public void showTitle(Title title) {
    }

    @Override
    public void sendMessage(String message) {
    }

    @Override
    public void setGameMode(GameMode gameMode) {
    }

    @Override
    public PermissionAttachment addAttachment(JavaPlugin plugin, String permission, boolean value) {
        return new PermissionAttachment(plugin, permission, value);
    }
}
