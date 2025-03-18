package info.cho.passwords.commands;

import info.cho.passwords.Passwords;
import info.cho.passwords.config.Config;
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
@Command(value = "setpassword")
@CommandPresence(DefaultPresenceProvider.class)
public class SetPasswordCommand extends BaseCommand {

    @Command("#")
    public void execute(CommandContext context, @Arg("player") Player player, @Arg("password") String password) {
        if (player == null) {
            context.sendMessage(MessageType.INFO, "§cPlayer not found!");
            return;
        }
        if (password == null || password.isEmpty()) {
            context.sendMessage(MessageType.INFO,"§cPassword is null or empty!");
            return;
        }

        if (password.length() != Passwords.config.getPasswordLength()) {
            context.sendMessage(MessageType.ERROR, "§cPassword length is incorrect!");
            return;
        }

        Passwords.getDataManager().setPlayerValue(player, "playerPassword", password);
        context.sendMessage(MessageType.INFO, "§aPassword for " + player.getName() + " has been successfully set!");

    }
}
