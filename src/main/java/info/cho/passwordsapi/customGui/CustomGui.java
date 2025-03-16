package info.cho.passwordsapi.customGui;

import info.cho.passwords.Passwords;
import info.cho.passwords.config.Config;
import info.cho.passwords.util.DataManager;
import org.bukkit.entity.Player;

public class CustomGui {

    public DataManager dataManager;
    public Passwords passwords;
    public Config config;

    public CustomGui() {
        this.passwords = Passwords.instance;
        this.dataManager = Passwords.dataManager;
        this.config = Passwords.config;
    }


    /**
     * Gets the charter.
     * @param index the slot.
     * @param player player.
     * @return integer.
     */
    public int getChar(int index, Player player) {
        return (int) dataManager.getPlayerValue(player, "char" + 1);
    }

    /**
     * Register the characters.
     * @param player Player.
     */
    public void registerChar(Player player) {
        int passwordLength = config.getPasswordLength();
        for (int i = 0; i < passwordLength; i++) {
            dataManager.setPlayerValue(player, "char" + i, null);
        }
    }

    /**
     * Set login state
     * @param login Boolean.
     * @param player Player.
     */
    public void setLogin(boolean login, Player player) {
        dataManager.setPlayerValue(player, "isLogIn", login);
    }

    /**
     * Get login state
     * @param player The player.
     * @return logins state.
     */
    public boolean getLogin(Player player) {
        return (boolean) dataManager.getPlayerValue(player, "isLogIn");
    }

}
