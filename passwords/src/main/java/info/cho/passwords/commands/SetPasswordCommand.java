package info.cho.passwords.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import info.cho.passwords.commands.argument.PasswordArgument;
import info.cho.passwords.utls.DataManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SetPasswordCommand {

    private SetPasswordCommand() {}

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("setpassword")
                .requires(src -> src.getSender() instanceof Player)
                .then(Commands.argument("password", new PasswordArgument())
                        .executes(ctx -> {
                            final Player player = (Player) ctx.getSource().getSender();
                            final String password = ctx.getArgument("password", String.class);

                            final DataManager dataManager = new DataManager();
                            dataManager.setPlayerValue(player, "password", password);

                            player.kick(Component.text("Password changed!", NamedTextColor.DARK_GREEN));
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }
}
