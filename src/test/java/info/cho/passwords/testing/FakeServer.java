package info.cho.passwords.testing;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.SimpleBukkitScheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeServer implements Server {
    private final FakePluginManager pluginManager = new FakePluginManager();
    private final BukkitScheduler scheduler = new SimpleBukkitScheduler();
    private final List<Player> players = new ArrayList<>();

    @Override
    public FakePluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public BukkitScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public Collection<Player> getOnlinePlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void clearPlayers() {
        players.clear();
    }
}
