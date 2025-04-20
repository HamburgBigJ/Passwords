package cho.info.passwords.player.commands;

import cho.info.passwords.Passwords;
import cho.info.passwords.utls.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetPlayerPasswords implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 2) {
            String playerName = strings[0];
            String password = strings[1];

            // Get the player by name
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                commandSender.sendMessage("Player not found!");
                return true;
            }

            // Save password using DataManager
            Passwords.dataManager.setPlayerValue(player, "playerPassword", password); // Assuming this method exists in DataManager
            commandSender.sendMessage("Password set for player " + playerName);

        } else {
            commandSender.sendMessage("Usage: /setpassword <player> <password>");
        }

        return true;
    }
}
