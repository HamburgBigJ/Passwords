package org.bukkit.event.player;

import org.bukkit.entity.Player;

public class PlayerQuitEvent extends PlayerEvent {
    public PlayerQuitEvent(Player player) {
        super(player);
    }
}
