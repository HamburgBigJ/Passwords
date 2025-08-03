package info.cho.passwords.utls;

import info.cho.passwords.Passwords;
import info.cho.passwordsApi.password.PasswordConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class PLog {

    public static void info(String message) {
        Passwords.instance.getLogger().info(message);
    }

    public static void warning(String message) {
        Passwords.instance.getLogger().warning("[WARNING] " + message);
    }

    public static void debug(String message) {
        if (PasswordConfig.isDebugMode()) {
            Passwords.instance.getLogger().warning("[PSDebug] " + message);
        }
    }

}
