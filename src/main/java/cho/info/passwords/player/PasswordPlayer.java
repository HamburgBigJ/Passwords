package cho.info.passwords.player;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.ConfigManager;

public class PasswordPlayer {

    public ConfigManager configManager;
    public Passwords passwords;

    public PasswordPlayer(ConfigManager configManager, Passwords passwords) {
        this.configManager = configManager;
        this.passwords = passwords;
    }

    public void listeners() {
        passwords.getServer().getPluginManager().registerEvents(new PlayerPasswordsListener(passwords, configManager), passwords);
    }

}
