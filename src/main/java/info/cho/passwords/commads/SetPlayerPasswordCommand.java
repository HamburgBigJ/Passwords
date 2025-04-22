package info.cho.passwords.commads;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import info.cho.passwords.commads.argument.PasswordArgument;
import info.cho.passwords.utls.DataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class SetPlayerPasswordCommand {

    public SetPlayerPasswordCommand() {
        new CommandAPICommand("setplayerpassword")
                .withArguments(new PlayerArgument("player"))
                .withArguments(new PasswordArgument("password"))
                .withPermission("passwords.command.setplayerpassword")
                .executes((sender, args) -> {
                    Player player = (Player) args.get("player");
                    DataManager dataManager = new DataManager();
                    String password = (String) args.get("password");
                    dataManager.setPlayerValue(player, "password", password);
                    player.kick(Component.text("Password set successfully!", NamedTextColor.GREEN));
                })
                .register();
    }

}
