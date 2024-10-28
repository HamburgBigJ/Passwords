package cho.info.passwords.universal;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.ConfigManager;

public class PasswordsUniversal {

    public Passwords passwords;
    public ConfigManager configManager;

    public PasswordsUniversal(Passwords passwords, ConfigManager configManager) {
        this.passwords = passwords;
        this.configManager = configManager;
    }

    public void listeners() {
        passwords.getServer().getPluginManager().registerEvents(new PasswordsUniversalPlayerLeave(configManager), passwords);
    }

}
