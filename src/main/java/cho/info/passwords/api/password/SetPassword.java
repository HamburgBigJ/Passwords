package cho.info.passwords.api.password;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.ConfigManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class SetPassword {

    public ConfigManager configManager;
    public Passwords passwords;
    private final Logger logger;

    public SetPassword(ConfigManager configManager, Passwords passwords) {
        this.configManager = configManager;
        this.passwords = passwords;
        this.logger = passwords.getLogger(); // Initialize logger in the constructor
    }

    public void setPlayerPassword(Player player, int password) {
        int maxLength = passwords.getConfig().getInt("settings.password-length");
        int passwordLength = String.valueOf(password).length();

        // Validate password length
        if (passwordLength > maxLength) {
            logger.warning("Password is too long! Maximum allowed length: " + maxLength);
            return; // Stop execution if the password is too long
        }


        // Set the player's password
        configManager.setPlayerValue(player, "password", password);
        logger.info("Password for player " + player.getName() + " has been successfully updated.");

        // Kick the player if required
        if (passwords.getConfig().getBoolean("kick-password-change") && player.isOnline()) {
            player.kick(Component.text("Your password has been changed!"));
        }
    }

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

}
