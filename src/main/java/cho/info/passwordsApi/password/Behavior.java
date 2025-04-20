package cho.info.passwordsApi.password;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.DataManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public class Behavior {

    public DataManager dataManager;
    public Passwords passwords;

    public Behavior() {
        this.dataManager = Passwords.dataManager;
        this.passwords = Passwords.instance;
    }

    /**
     * @param isLogin boolean
     * @param player Player
     */
    public void setLogin(Boolean isLogin, Player player) {
        dataManager.setPlayerValue(player, "isLogIn", isLogin);
    }

    /**
     * @param listener Listener
     */
    public void registerEvents(Listener listener) {
        passwords.getServer().getPluginManager().registerEvents(listener, passwords);
    }

    /**
     * @param commandExecutor CommandExecutor
     * @param command String
     */
    public void registerCommand(CommandExecutor commandExecutor, String command) {
        passwords.getCommand(command).setExecutor(commandExecutor);
    }

    /**
     * @param player Player
     * @return boolean
     */
    public boolean isLoginScreen(Player player) {
        return (boolean) dataManager.getPlayerValue(player, "isLogIn");
    }

    /**
     * Do not change the name of the Inventory
     * @return Inventory
     */
    public Inventory getInventory() {
        return passwords.getInventory();
    }

    /**
     * Do not change the name of the Inventory
     * @return Inventory
     */
    public Inventory getFirstLoginInventory() {
        return passwords.getFirstJoinInventory();
    }


    /**
     * @param player Player
     * @param reason String
     */
    public void kickPlayer(Player player, String reason) {
        player.kickPlayer(passwords.getConfig().getString("api.kick-player-message") + reason);
    }
}
