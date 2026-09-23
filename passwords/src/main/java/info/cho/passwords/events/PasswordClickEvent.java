package info.cho.passwords.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PasswordClickEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String behaviorName;
    private final int number;

    public PasswordClickEvent(Player player, String behaviorName, int number) {
        this.player = player;
        this.behaviorName = behaviorName;
        this.number = number;
    }

    public Player getPlayer() {
        return player;
    }

    public String getBehaviorName() {
        return behaviorName;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}