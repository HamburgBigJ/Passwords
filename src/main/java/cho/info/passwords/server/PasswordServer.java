package cho.info.passwords.server;

import cho.info.passwords.Passwords;
import cho.info.passwords.server.customgui.CustomGuiHandler;
import cho.info.passwords.utls.DataManager;

public class PasswordServer {

    public void listeners() {

        Passwords.instance.getServer().getPluginManager().registerEvents(new PlayerLeave(), Passwords.instance);
        Passwords.instance.getServer().getPluginManager().registerEvents(new ServerPasswordsListener(), Passwords.instance);
        Passwords.instance.getServer().getPluginManager().registerEvents(new CustomGuiHandler(), Passwords.instance);

    }

}
