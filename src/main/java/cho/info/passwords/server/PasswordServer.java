package cho.info.passwords.server;

import cho.info.passwords.Passwords;
import cho.info.passwords.server.customgui.CustomGuiHandler;
import cho.info.passwords.utls.DataManager;

public class PasswordServer {

    public Passwords passwords;
    public DataManager dataManager;


    public PasswordServer(Passwords passwords, DataManager dataManager) {
        this.passwords = passwords;
        this.dataManager = dataManager;
    }

    public void listeners() {
        
        passwords.getServer().getPluginManager().registerEvents(new ServerPasswordsListener(passwords, dataManager), passwords);
        passwords.getServer().getPluginManager().registerEvents(new CustomGuiHandler(passwords, dataManager), passwords);

    }

}

