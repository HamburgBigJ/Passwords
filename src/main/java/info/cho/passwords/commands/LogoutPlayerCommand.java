package info.cho.passwords.commands;

import io.fairyproject.bukkit.command.presence.DefaultPresenceProvider;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.CommandContext;
import io.fairyproject.command.MessageType;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.command.annotation.CommandPresence;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;

@InjectableComponent
@Command(value = "logout")
@CommandPresence(DefaultPresenceProvider.class)
public class LogoutPlayerCommand extends BaseCommand {

    @Command("#")
    public void playerLogout(CommandContext context, @Arg("player") Player player) {
        if (player == null) {
            context.sendMessage(MessageType.valueOf("§cPlayer not found"));
            return;
        }

        player.kickPlayer("§cYou have been logged out!");
        context.sendMessage(MessageType.valueOf("§a" + player.getName() + " has been logged out!"));
    }


}
