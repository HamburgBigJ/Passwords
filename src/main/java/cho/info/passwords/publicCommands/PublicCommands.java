package cho.info.passwords.publicCommands;

import cho.info.passwords.Passwords;
import cho.info.passwords.publicCommands.player.LogoutPlayer;
import cho.info.passwords.publicCommands.reload.PasswordsReloadCommand;

public class PublicCommands {


    public void registerCommands() {
        Passwords.instance.getCommand("preload").setExecutor(new PasswordsReloadCommand());
        Passwords.instance.getCommand("logout").setExecutor(new LogoutPlayer());
    }

}
