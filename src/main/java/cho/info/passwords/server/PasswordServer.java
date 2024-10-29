package cho.info.passwords.server;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.ConfigManager;

public class PasswordServer {

    public Passwords passwords;
    public ConfigManager configManager;


    public PasswordServer(Passwords passwords, ConfigManager configManager) {
        this.passwords = passwords;
        this.configManager = configManager;
    }

    public void listeners() {
        
        passwords.getServer().getPluginManager().registerEvents(new ServerPasswordsListener(passwords, configManager), passwords);


    }

}
