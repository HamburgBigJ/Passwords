package info.cho.passwords.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
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
public class SetPlayerPasswordCommand {

    private SetPlayerPasswordCommand() {}

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("setplayerpassword")
                .requires(src -> src.getSender().hasPermission("passwords.command.setplayerpassword"))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .then(Commands.argument("password", StringArgumentType.word())
                                .executes(ctx -> {


                                    final PlayerSelectorArgumentResolver resolver =
                                            ctx.getArgument("player", PlayerSelectorArgumentResolver.class);

                                    final Player player = resolver.resolve(ctx.getSource()).getFirst();
                                    final String password = StringArgumentType.getString(ctx, "password");


                                    final DataManager dataManager = new DataManager();
                                    dataManager.setPlayerValue(player, "password", password);

                                    player.kick(Component.text("Password set successfully!", NamedTextColor.DARK_GREEN));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .build();
    }
}
