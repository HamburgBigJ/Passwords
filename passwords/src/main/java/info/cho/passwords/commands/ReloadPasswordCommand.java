package info.cho.passwords.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import info.cho.passwords.Passwords;
import info.cho.passwords.utls.PLog;
import info.cho.passwordsApi.PasswordsApi;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ReloadPasswordCommand {

    private ReloadPasswordCommand() {}

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("preload")
                .executes(context -> {
                    PLog.info("Reload Config");

                    Passwords.instance.reloadConfig();
                    PasswordsApi.getCustomGuiHandler().updateCurrentMode();

                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
