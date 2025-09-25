package info.cho.passwords.testing;

import org.bukkit.Bukkit;

public final class TestEnvironment {
    private TestEnvironment() {
    }

    public static FakeServer createServer() {
        FakeServer server = new FakeServer();
        Bukkit.setServer(server);
        return server;
    }
}
