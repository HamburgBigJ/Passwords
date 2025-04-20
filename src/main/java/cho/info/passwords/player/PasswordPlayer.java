package cho.info.passwords.player;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.DataManager;

public class PasswordPlayer {


    public void listeners() {
        Passwords.instance.getServer().getPluginManager().registerEvents(new PlayerPasswordsListener(), Passwords.instance);
    }

}
