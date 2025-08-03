package info.cho.passwords.commads;

import dev.jorel.commandapi.CommandAPICommand;
import info.cho.passwords.commads.argument.PasswordArgument;
import info.cho.passwords.utls.DataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class SetPasswordCommand {

    public SetPasswordCommand() {
        new CommandAPICommand("setpassword")
                .withArguments(new PasswordArgument("password"))
                .executes((sender, args) -> {
                    String password = (String) args.get("password");
                    DataManager dataManager = new DataManager();
                    dataManager.setPlayerValue((Player) sender, "password", password);
                    ((Player) sender).kick(Component.text("Password changed!", NamedTextColor.DARK_GREEN));
                })
                .register();
    }

}
