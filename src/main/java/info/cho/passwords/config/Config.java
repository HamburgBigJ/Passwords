package info.cho.passwords.config;

import info.cho.passwords.Passwords;
import io.fairyproject.config.annotation.Comment;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Inherited;

@Getter
@Setter
@InjectableComponent
public class Config extends YamlConfiguration {

    @Comment({"check-type: server : One password for the entire server." ,
            "check-type: player : A unique password for each player. (Works only on the first join. To reset, delete player data or use the /setpassword command)",
            "check-type: custom : The addon will handle the ui / mode. (You can use the api to change the password)"})
    private String checkType = "server";

    @Comment("Gui Name")
    private String guiName = "§9Password";

    @Comment("Set password name")
    private String setPasswordName = "§aSet Password";

    @Comment("Message that will be displayed as the kick reason.")
    private String failMessage = "§4§lThe password is incorrect!";

    @Comment("When the UI is closed without a password")
    private String closeUiMessage = "§4§lYou need to enter a Password!";

    @Comment("Enable welcome message.")
    private boolean welcomeMessageEnabled = true;

    @Comment("Welcome message after login.")
    private String welcomeMessage = "§3Welcome to ExampleServer";

    @Comment("Welcomemassage second line (Only for title)")
    private String welcomeMessageSecond = "§2Passwords";

    @Comment("Movement event cancel")
    private String messageKickMovement = "§4§lYou can not move withot being login!";

    @Comment("Password length")
    private int passwordLength = 4;

    @Comment("Display type for the welcome message: chat; actionbar; title")
    private String welcomeMessageDisplayType = "chat";

    @Comment("Admin password enable")
    private boolean adminPasswordEnabled = true;

    @Comment("Admin password grants all permissions to the player. (Can not be longer than the password-length)")
    private String adminPassword = "8143";

    @Comment("Admin login as Op.")
    private boolean isAdminOp = true;

    @Comment("Enable login gamemode.")
    private boolean loginGamemodeEnabled = true;

    @Comment("Gamemode every player will have upon login: survival, creative, adventure, spectator")
    private String loginGamemode = "survival";

    @Comment("Prevents movement for non login player")
    private boolean preventsMovement = true;

    @Comment("Ip Login. Login with ip ( Beta - Mey not work!)")
    private boolean loginIp = false;

    @Comment("DiscordSRV support")
    private boolean useDiscordSrv = false;

    @Comment("DiscordSRV Password on login")
    private boolean discordNeedPassword = true;

    @Comment("Server password (Can not be longer than the password-length)")
    private String serverPassword = "1234";

    @Comment("Enable the API")
    private boolean enableApi = true;

    @Comment("Kicks the player when the password changes.")
    private boolean kickPasswordChangeApi = true;

    @Comment("Change Password Message")
    private String changePasswordMessageApi = "§4Your password has benn changed!";

    @Comment("Kick Player Message")
    private String kickPlayerMessageApi = "§6 Passwords Kicked You Reason : ";

    @Comment("Override the type. (Only for custom)")
    private boolean overrideCheckTypeApi = false;

    public Config() {
        super(Passwords.instance.getDataFolder().toAbsolutePath().resolve("config.yml"));
        this.loadAndSave();
    }
}