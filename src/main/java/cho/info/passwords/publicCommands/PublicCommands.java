package cho.info.passwords.publicCommands;

import cho.info.passwords.Passwords;

public class PublicCommands {

    public Passwords passwords;

    public PublicCommands(Passwords passwords) {
        this.passwords = passwords;
    }

    public void registerCommands() {
        passwords.getCommand("preload").setExecutor(new PasswordsReloadCommand(passwords));
    }

}
