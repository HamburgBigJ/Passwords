package info.cho.passwords.commads.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.CommandAPIArgumentType;
import dev.jorel.commandapi.executors.CommandArguments;
import info.cho.passwordsApi.password.PasswordConfig;

public class PasswordPlayerArgument extends Argument<String> {


    private static final int MAX_LENGTH = PasswordConfig.getPlayerPasswordLength();
    private static final int MIN_LENGTH = 1;
    private static final String ALLOWED_CHARACTERS = "123456789";

    public PasswordPlayerArgument(String nodeName) {
        super(nodeName, StringArgumentType.word());
    }

    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public CommandAPIArgumentType getArgumentType() {
        return CommandAPIArgumentType.PRIMITIVE_STRING;
    }

    @Override
    public <Source> String parseArgument(CommandContext<Source> cmdCtx, String key, CommandArguments previousArgs) throws CommandSyntaxException {
        String password = cmdCtx.getArgument(key, String.class);

        if (PasswordConfig.getBlockedPasswordList().contains(password)) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException()
                    .create("Invalid password. Choose a different password.");
        }

        if (!isValidPassword(password)) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException()
                    .create("Invalid password. It must be " + MAX_LENGTH + " characters and contain only allowed characters ( 1 - 9).");
        }

        return password;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            return false;
        }

        for (char c : password.toCharArray()) {
            if (ALLOWED_CHARACTERS.indexOf(c) == -1) {
                return false;
            }
        }

        return true;
    }

}
