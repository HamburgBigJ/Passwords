package cho.info.passwords.player;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.ConfigManager;
import org.bukkit.event.Listener;

public class PlayerPasswordsListener implements Listener {

    public ConfigManager configManager;
    public Passwords passwords;

    public PlayerPasswordsListener(Passwords passwords, ConfigManager configManager) {
        this.passwords = passwords;
        this.configManager = configManager;
    }


}