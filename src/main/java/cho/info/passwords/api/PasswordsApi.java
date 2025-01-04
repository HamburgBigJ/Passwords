package cho.info.passwords.api;

import cho.info.passwords.Passwords;
import cho.info.passwords.api.password.Behavior;
import cho.info.passwords.api.password.Config;
import cho.info.passwords.api.password.SetPassword;
import cho.info.passwords.utls.DataManager;

public class PasswordsApi {

    public Passwords passwords;
    public DataManager dataManager;

    public PasswordsApi(Passwords passwords, DataManager dataManager) {
        this.passwords = passwords;
        this.dataManager = dataManager;
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
        return new SetPassword(dataManager, passwords);
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
        return new Behavior(dataManager, passwords);
    }

    /**
     * Get the config manager
     * @return DataManager
     */
    public DataManager getDataManager(){
        return dataManager;
    }

}
