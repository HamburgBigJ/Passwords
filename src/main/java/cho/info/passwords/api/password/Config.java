package cho.info.passwords.api.password;

import cho.info.passwords.Passwords;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public Passwords passwords;

    public Config(Passwords passwords) {
        this.passwords = passwords;
    }

    /**
     * Get the password length
     * @param length The length of the password
     */
    public void setPasswordLength(int length) {
        passwords.getConfig().set("settings.password-length", length);
    }

    /**
     * Get the password length
     * @param gamemode The gamemode of the player
     */
    public void setLoginGamemode(String gamemode) {
        passwords.getConfig().set("settings.login-gamemode", gamemode);
    }

    public void setAdminOpLogin(boolean adminOpLogin) {
        passwords.getConfig().set("settings.is-admin-op", adminOpLogin);
    }


    public FileConfiguration getConfig() {
        return passwords.getConfig();
    }
}
