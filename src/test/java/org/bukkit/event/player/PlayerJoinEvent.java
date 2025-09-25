package org.bukkit.event.player;

import org.bukkit.entity.Player;

public class PlayerJoinEvent extends PlayerEvent {
    public PlayerJoinEvent(Player player) {
        super(player);
    }
}
