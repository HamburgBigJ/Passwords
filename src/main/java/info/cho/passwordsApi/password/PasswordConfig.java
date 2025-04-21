package info.cho.passwordsApi.password;

import info.cho.passwords.Passwords;
import org.bukkit.configuration.file.FileConfiguration;

public class PasswordConfig {

    private static FileConfiguration config() {
        return Passwords.instance.getConfig();
    }

    // General Settings
    public static boolean isEnabled() {
        return config().getBoolean("enable");
    }

    public static String getVersion() {
        return config().getString("version");
    }

    public static boolean isDebugMode() {
        return config().getBoolean("debug");
    }

    // Settings
    public static String getCheckType() {
        return config().getString("settings.check-type");
    }

    public static String getGuiName() {
        return config().getString("settings.gui-name");
    }

    public static String getSetPasswordName() {
        return config().getString("settings.set-password-name");
    }

    public static String getFailMessage() {
        return config().getString("settings.fail-message");
    }

    public static String getCloseUiMessage() {
        return config().getString("settings.close-ui-message");
    }

    public static boolean isWelcomeMessageEnabled() {
        return config().getBoolean("settings.welcome-message-enabled");
    }

    public static String getWelcomeMessage() {
        return config().getString("settings.welcome-message");
    }

    public static String getWelcomeMessageSecondLine() {
        return config().getString("settings.welcome-message-second");
    }

    public static String getWelcomeMessageDisplayType() {
        return config().getString("settings.welcome-message-display-type");
    }

    public static boolean isLoginGamemodeEnabled() {
        return config().getBoolean("settings.login-gamemode-enabled");
    }

    public static String getLoginGamemode() {
        return config().getString("settings.login-gamemode");
    }

    public static boolean isMovementPrevented() {
        return config().getBoolean("settings.prevents-movement");
    }

    // Discord Settings
    public static boolean isDiscordSrvEnabled() {
        return config().getBoolean("discord.need-password");
    }

    // Server Settings
    public static String getServerPassword() {
        return config().getString("server.password");
    }

    public static int getPasswordLength() {
        String password = getServerPassword();
        return password != null ? password.length() : 0;
    }
}