package info.cho.passwordsApi;

import info.cho.passwords.Passwords;
import info.cho.passwords.customGui.CustomGui;
import info.cho.passwords.utls.DataManager;

public class PasswordsApi {

    public Passwords passwords;
    public DataManager dataManager;

    public PasswordsApi() {
        this.passwords = Passwords.instance;
        this.dataManager = new DataManager();
    }

    /**
     * Get the plugin
     * @return Passwords
     */
    public Passwords getPlugin(){
        return passwords;
    }

    /**
     * Get the config manager
     * @return DataManager
     */
    public DataManager getDataManager(){
        return dataManager;
    }


    /**
     * Get the custom gui manager
     * @return CustomGui
     */
    public CustomGui getCustomGui(){
        return Passwords.customGui;
    }

}
