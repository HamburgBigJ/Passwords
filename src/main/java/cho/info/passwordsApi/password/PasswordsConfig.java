package cho.info.passwordsApi.password;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.DataManager;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class PasswordsConfig {

    private final Passwords passwords;
    private final DataManager dataManager;
    private final Logger logger;

    public PasswordsConfig() {
        this.passwords = Passwords.instance;
        this.dataManager = Passwords.dataManager;
        this.logger = passwords.getLogger();
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

    /**
     * @return Plugin Config
     */
    public FileConfiguration getConfig() {
        return passwords.getConfig();
    }

    /**
     * Sets player Password
     * @param player Player
     * @param password int
     */

    public void setPlayerPassword(Player player, int password) {
        int maxLength = passwords.getConfig().getInt("settings.password-length");
        int passwordLength = String.valueOf(password).length();

        // Validate password length
        if (passwordLength > maxLength) {
            logger.warning("Password is too long! Maximum allowed length: " + maxLength);
            return; // Stop execution if the password is too long
        }


        // Set the player's password
        dataManager.setPlayerValue(player, "password", password);
        logger.info("Password for player " + player.getName() + " has been successfully updated.");

        // Kick the player if required
        if (passwords.getConfig().getBoolean("kick-password-change") && player.isOnline()) {
            player.kick(Component.text("Your password has been changed!"));
        }
    }

    /**
     * set server password
     * @param password int
     * @param reason String
     */

    public void setServerPassword(int password, String reason) {
        int maxLength = passwords.getConfig().getInt("settings.password-length");
        int passwordLength = String.valueOf(password).length();

        if (passwordLength > maxLength) {
            logger.warning("Password is too long! Maximum allowed length: " + maxLength);
            return;
        }

        // Set the server's password
        passwords.getConfig().set("server.password", password);
    }

    /**
     * set admin password
     * @param password int
     */

    public void setAdminPassword (int password) {
        int maxLength = passwords.getConfig().getInt("settings.password-length");
        int passwordLength = String.valueOf(password).length();

        // Validate password length
        if (passwordLength > maxLength) {
            logger.warning("Admin password is too long! Maximum allowed length: " + maxLength);
            return; // Stop execution if the password is too long
        }

        passwords.getConfig().set("settings.admin-password", password);
    }

    /**
     * set player password
     * @param player Player
     * @return password
     */
    public String getPlayerPassword(Player player) {
        return (String) dataManager.getPlayerValue(player, "playerPassword");
    }

    /**
     * @return Admin password
     */
    public String getAdminPassword() {
        return passwords.getConfig().getString("settings.admin-password");
    }

    /**
     * @return Server password
     */
    public String getServerPassword() {
        return passwords.getConfig().getString("server.password");
    }
}
