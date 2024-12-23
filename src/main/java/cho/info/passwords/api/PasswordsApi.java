package cho.info.passwords.api;

import cho.info.passwords.Passwords;
import cho.info.passwords.api.password.Behavior;
import cho.info.passwords.api.password.Config;
import cho.info.passwords.api.password.SetPassword;
import cho.info.passwords.utls.ConfigManager;

public class PasswordsApi {

    public Passwords passwords;
    public ConfigManager configManager;

    public PasswordsApi(Passwords passwords, ConfigManager configManager) {
        this.passwords = passwords;
        this.configManager = configManager;
    }

    /**
     * Get the plugin
     * @return Passwords
     */
    public Passwords getPlugin(){
        return passwords;
    }

    /**
     * Get the set password
     * @return SetPassword
     */
    public SetPassword setPassword(){
        return new SetPassword(configManager, passwords);
    }

    /**
     * Get the config
     * @return Config
     */
    public Config config(){
        return new Config(passwords);
    }

    /**
     * Get the behavior
     * @return Behavior
     */
    public Behavior behavior(){
        return new Behavior(configManager, passwords);
    }

    /**
     * Get the config manager
     * @return ConfigManager
     */
    public ConfigManager getConfigManager(){
        return configManager;
    }

}
