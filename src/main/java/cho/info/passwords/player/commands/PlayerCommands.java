package cho.info.passwords.player.commands;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.DataManager;

public class PlayerCommands {


    public void registerPlayerCommands() {
        
         Passwords.instance.getCommand("setpassword").setExecutor(new SetPlayerPasswords());
    }
}
