package cho.info.passwords.publicCommands;

import cho.info.passwords.Passwords;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PasswordsReloadCommand implements CommandExecutor {

    public Passwords passwords;

    public PasswordsReloadCommand(Passwords passwords) {
        this.passwords = passwords;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        commandSender.sendMessage(ChatColor.BLUE + "Reload Passwords ...");

        passwords.reload(commandSender);

        return false;
    }
}
