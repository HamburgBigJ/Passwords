package cho.info.passwords.publicCommands.player;

import cho.info.passwords.Passwords;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LogoutPlayer implements CommandExecutor {


    public Passwords passwords;

    public LogoutPlayer(Passwords passwords) {
        this.passwords = passwords;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 0) {
            Player target = commandSender.getServer().getPlayer(strings[0]);
            target.kick(Component.text(passwords.getConfig().getString("settings.logout-message")));
        } else if (strings.length == 1) {
            Player target = commandSender.getServer().getPlayer(strings[0]);
            target.kick(Component.text(strings[1]));
        }

        return false;
    }
}
