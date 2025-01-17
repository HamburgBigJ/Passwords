package cho.info.passwords.publicCommands;

import cho.info.passwords.Passwords;
import cho.info.passwords.publicCommands.player.LogoutPlayer;
import cho.info.passwords.publicCommands.reload.PasswordsReloadCommand;

public class PublicCommands {

    public Passwords passwords;

    public PublicCommands(Passwords passwords) {
        this.passwords = passwords;
    }

    public void registerCommands() {
        passwords.getCommand("preload").setExecutor(new PasswordsReloadCommand(passwords));
        passwords.getCommand("logout").setExecutor(new LogoutPlayer(passwords));
    }

}
