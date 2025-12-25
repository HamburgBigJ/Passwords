package info.cho.passwords.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import info.cho.passwords.commands.argument.PasswordPlayerArgument;
import info.cho.passwords.utls.DataManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class LogoutPlayerCommand {

    private LogoutPlayerCommand() {}

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("logout")
                .requires(src -> src.getSender().hasPermission("passwords.command.logout"))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(ctx -> {
                            final PlayerSelectorArgumentResolver targetResolver =
                                    ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                            final Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                            final DataManager dataManager = new DataManager();
                            dataManager.setPlayerValue(target, "isLogin", false);

                            target.kick(Component.text(ctx.getSource().getSender().getName() + " has logged you out!", NamedTextColor.RED));
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }
}
