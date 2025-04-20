package cho.info.passwords.utls;

import cho.info.passwords.Passwords;

public class PLog {

    public static void info(String message) {
        Passwords.instance.getLogger().info(message);
    }

    public static void warning(String message) {
        Passwords.instance.getLogger().warning(message);
    }

    public static void debug(String message) {
        if (Passwords.instance.getConfig().getBoolean("debug")) {
            Passwords.instance.getLogger().warning("[PSDebug] " + message);
        }
    }

}
