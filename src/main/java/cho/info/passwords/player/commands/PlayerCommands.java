package cho.info.passwords.player.commands;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.ConfigManager;

public class PlayerCommands {

    public Passwords passwords;
    public ConfigManager configManager;

    public PlayerCommands(Passwords passwords, ConfigManager configManager) {
        this.passwords = passwords;
        this.configManager = configManager;
    }

    public void registerPlayerCommands() {
        passwords.getCommand("setpassword").setExecutor(new SetPlayerPasswords(configManager));
    }
}
