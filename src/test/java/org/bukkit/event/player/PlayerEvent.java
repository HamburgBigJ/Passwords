package org.bukkit.event.player;

import org.bukkit.entity.Player;

public abstract class PlayerEvent {
    private final Player player;

    protected PlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
