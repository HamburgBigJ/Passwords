package cho.info.passwords.api;

import cho.info.passwords.Passwords;
import cho.info.passwords.api.password.SetPassword;
import cho.info.passwords.utls.ConfigManager;

public class PasswordsApi {

    public Passwords passwords;
    public ConfigManager configManager;

    public PasswordsApi(Passwords passwords, ConfigManager configManager) {
        this.passwords = passwords;
        this.configManager = configManager;
    }

    public Passwords getPlugin(){
        return passwords;
    }

    public SetPassword setPassword(){
        return new SetPassword(configManager, passwords);
    }

}
