package cho.info.passwords.api.password;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Behavior {

    public ConfigManager configManager;
    public Passwords passwords;

    public Behavior(ConfigManager configManager, Passwords passwords) {
        this.configManager = configManager;
        this.passwords = passwords;
    }

    /**
     * @param isLogin boolean
     * @param player Player
     */
    public void setLogin(Boolean isLogin, Player player) {
        configManager.setPlayerValue(player, "isLogIn", isLogin);
    }

    /**
     * @param listener Listener
     */
    public void registerEvents(Listener listener) {
        passwords.getServer().getPluginManager().registerEvents(listener, passwords);
    }

    /**
     * @param player Player
     * @return boolean
     */
    public boolean isLoginScreen(Player player) {
        return (boolean) configManager.getPlayerValue(player, "isLogIn");
    }

    public void resetConfig(String consoleMessage) {
        passwords.getLogger().info(consoleMessage);

        passwords.configUpdate();
    }
}
