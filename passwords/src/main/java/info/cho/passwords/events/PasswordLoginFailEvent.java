package info.cho.passwords.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PasswordLoginFailEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final String behaviorName;

    public PasswordLoginFailEvent(Player player, String behaviorName) {
        this.player = player;
        this.behaviorName = behaviorName;
    }

    public Player getPlayer() {
        return player;
    }

    public String getBehaviorName() {
        return behaviorName;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
