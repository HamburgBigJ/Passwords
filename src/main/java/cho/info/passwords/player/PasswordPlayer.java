package cho.info.passwords.player;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.DataManager;

public class PasswordPlayer {

    public DataManager dataManager;
    public Passwords passwords;

    public PasswordPlayer(DataManager dataManager, Passwords passwords) {
        this.dataManager = dataManager;
        this.passwords = passwords;
    }

    public void listeners() {
        passwords.getServer().getPluginManager().registerEvents(new PlayerPasswordsListener(passwords, dataManager), passwords);
    }

}
