package info.cho.passwords.utls;

import info.cho.passwords.Passwords;
import info.cho.passwordsApi.password.PasswordConfig;

public class PLog {

    public static void info(String message) {
        Passwords.instance.getLogger().info(message);
    }

    public static void warning(String message) {
        Passwords.instance.getLogger().warning(message);
    }

    public static void debug(String message) {
        if (PasswordConfig.isDebugMode()) {
            Passwords.instance.getLogger().warning("[PSDebug] " + message);
        }
    }

}
