package info.cho.passwords.commads;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import info.cho.passwords.utls.DataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class LogoutPlayerCommand {

    public LogoutPlayerCommand() {
        new CommandAPICommand("logout")
                .withArguments(new PlayerArgument("player"))
                .withPermission("passwords.command.logout")
                .executes((sender, args) -> {
                    Player player = (Player) args.get("player");
                    DataManager dataManager = new DataManager();
                    dataManager.setPlayerValue(player, "isLogin", false);
                    player.kick(Component.text(sender.getName() + " has logged you out!", NamedTextColor.RED));
                })
                .register();
    }

}
