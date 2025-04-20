package cho.info.passwords.publicCommands.reload;

import cho.info.passwords.Passwords;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PasswordsReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        commandSender.sendMessage("<blue>Reload Passwords ...");

        if (Passwords.instance.getConfig().getBoolean("settings.use-discord-srv")) {
            commandSender.sendMessage("You can't reload the plugin while DiscordSRV features are enabled!");
            commandSender.sendMessage("Please use /restart to reload the plugin.");
        } else {
            Passwords.instance.reload(commandSender);
        }



        return false;
    }
}
