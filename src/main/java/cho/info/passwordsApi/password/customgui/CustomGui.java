package cho.info.passwordsApi.password.customgui;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.DataManager;
import org.bukkit.entity.Player;

public class CustomGui {

    public DataManager dataManager;
    public Passwords passwords;

    public CustomGui() {
        this.passwords = Passwords.instance;
        this.dataManager = Passwords.dataManager;
    }

    /**
     * Get the plugin check type
     * @return String
     */
    public String getType() {
        return passwords.getConfig().getString("settings.check-type");
    }

    /**
     * Sets the type for the Type
     * @param type string
     */
    public void setType(String type) {
        if (!passwords.getConfig().getBoolean("api.override-check-type")) {
            passwords.getLogger().info("§4Override disabled");
            return;
        }
        passwords.getConfig().set("settings.check-type", type);
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
        int passwordLength = passwords.getConfig().getInt("settings.password-length");
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
