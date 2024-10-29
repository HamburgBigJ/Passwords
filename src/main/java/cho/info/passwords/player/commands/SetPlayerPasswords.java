package cho.info.passwords.player.commands;

import cho.info.passwords.utls.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SetPlayerPasswords implements CommandExecutor {

    public ConfigManager configManager;

    public SetPlayerPasswords(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 2) {

        } else {
            commandSender.sendMessage("Usage: /setpassword <player> <password>");
        }

        return false;
    }
}
