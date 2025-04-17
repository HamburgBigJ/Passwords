package info.cho.passwordsapi;

import info.cho.passwords.Passwords;
import info.cho.passwords.config.Config;
import info.cho.passwords.util.DataManager;
import lombok.Getter;

@Getter
public class PasswordApi {

    public static PasswordApi instance;

    public Config config;
    public DataManager dataManager;
    public Passwords passwords;

    public PasswordApi() {
        config = Passwords.config;
        dataManager = Passwords.dataManager;
        passwords = Passwords.instance;
    }

    /**
     * Get the password length.
     * @return password length.
     */
    public static String getPluginVersion() {
        return Passwords.pluginVersion;
    }



}
