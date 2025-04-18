package cho.info.passwords.player.commands;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.DataManager;

public class PlayerCommands {

    public Passwords passwords;
    public DataManager dataManager;

    public PlayerCommands(Passwords passwords, DataManager dataManager) {
        this.passwords = passwords;
        this.dataManager = dataManager;
    }

    public void registerPlayerCommands() {
        
        passwords.getCommand("setpassword").setExecutor(new SetPlayerPasswords(dataManager));
    }
}
