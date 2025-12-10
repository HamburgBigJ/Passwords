package info.cho.passwordsApi;

import info.cho.passwordsApi.password.PasswordConfig;
import info.cho.passwords.Passwords;
import info.cho.passwords.customGui.CustomGui;
import info.cho.passwords.utls.DataManager;

public class PasswordsApi {

    public static Passwords passwords = Passwords.instance;
    public static DataManager dataManager = new DataManager();

    /**
     * Get the plugin
     * @return Passwords
     */
    public static Passwords getPlugin(){
        return passwords;
    }

    /**
     * Get the config manager
     * @return DataManager
     */
    public static DataManager getDataManager(){
        return dataManager;
    }


    /**
     * Get the custom gui manager
     * @return CustomGui
     */
    public static CustomGui getCustomGui(){
        return Passwords.customGui;
    }

    /**
     * Get the version of the plugin
     * @return String
     */
    public static String getPluginVersion() {
        return Passwords.version;
    }

    /**
     * Get the version of the config
     * @return String
     */
    public static String getConfigVersion() {
        return PasswordConfig.getVersion();
    }


}
