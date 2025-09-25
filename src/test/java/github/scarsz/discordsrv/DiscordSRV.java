package github.scarsz.discordsrv;

import java.util.UUID;

public final class DiscordSRV {
    private static final DiscordSRV INSTANCE = new DiscordSRV();

    private DiscordSRV() {
    }

    public static DiscordSRV getPlugin() {
        return INSTANCE;
    }

    public AccountLinkManager getAccountLinkManager() {
        return new AccountLinkManager();
    }

    public static class AccountLinkManager {
        public String getDiscordId(UUID uuid) {
            return null;
        }
    }
}
