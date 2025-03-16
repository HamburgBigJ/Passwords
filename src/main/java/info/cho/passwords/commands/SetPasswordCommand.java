package info.cho.passwords.commands;

import info.cho.passwords.Passwords;
import io.fairyproject.bukkit.command.presence.DefaultPresenceProvider;
import io.fairyproject.command.CommandContext;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.command.annotation.CommandPresence;
import io.fairyproject.config.annotation.Comment;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;

@InjectableComponent
@Comment(value = "setpassword")
@CommandPresence(DefaultPresenceProvider.class)
public class SetPasswordCommand {

    @Command("#")
    public void playerLogout(CommandContext context, @Arg("player") Player player, @Arg("password") String password) {
        Passwords.getDataManager().setPlayerValue(player, "playerPassword", password);
    }

}
