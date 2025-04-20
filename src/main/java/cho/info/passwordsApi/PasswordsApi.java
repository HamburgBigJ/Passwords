package cho.info.passwordsApi;

import cho.info.passwords.Passwords;
import cho.info.passwordsApi.password.Behavior;
import cho.info.passwordsApi.password.PasswordsConfig;
import cho.info.passwordsApi.password.customgui.CustomGui;
import cho.info.passwords.utls.DataManager;

public class PasswordsApi {

    public Passwords passwords;
    public DataManager dataManager;

    public PasswordsApi() {
        this.passwords = Passwords.instance;
        this.dataManager = Passwords.dataManager;
    }

    /**
     * Get the plugin
     * @return Passwords
     */
    public Passwords getPlugin(){
        return passwords;
    }


    /**
     * Get the config
     * @return Config
     */
    public PasswordsConfig passwordsConfig(){
        return new PasswordsConfig();
    }

    /**
     * Get the behavior
     * @return Behavior
     */
    public Behavior behavior(){
        return new Behavior();
    }

    /**
     * Get the config manager
     * @return DataManager
     */
    public DataManager getDataManager(){
        return dataManager;
    }

    /**
     * Get the custom gui
     * @return CustomGui
     */
    public CustomGui customGui(){
        return new CustomGui();
    }


}
