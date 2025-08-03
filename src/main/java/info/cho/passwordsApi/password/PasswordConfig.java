package info.cho.passwordsApi.password;

import info.cho.passwords.Passwords;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

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


    public static int getPlayerPasswordLength() {
        return config().getInt("settings.player-password-length");
    }

    public static List<String> getBlockedPasswordList() {
        return config().getStringList("settings.blocked-passwords");
    }

    public static String getStaffPassword() {
        return config().getString("server.staff-password");
    }

    public static List<String> getStaffPermissions() {
        return config().getStringList("server.staff-permissions");
    }

    public static boolean isRemoveStaffPermissionsOnLogout() {
        return config().getBoolean("server.remove-staff-permissions-on-logout");
    }
    // DiscordHook Settings
    public static boolean isUseDiscordLogin() {
        return config().getBoolean("discord.useDiscordLogin");
    }

    // Server Settings
    public static String getServerPassword() {
        return config().getString("server.password");
    }

    public static int getPasswordLength() {
        String password = getServerPassword();
        return password != null ? password.length() : 0;
    }

    public static List<Object> getServerPatternList() {
        return (List<Object>) config().getList("server.pattern");
    }
}