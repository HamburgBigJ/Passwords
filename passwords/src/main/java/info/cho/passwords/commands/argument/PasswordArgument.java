package info.cho.passwords.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import info.cho.passwordsApi.password.PasswordConfig;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PasswordArgument implements CustomArgumentType<String, String> {

    private static final int MIN_LENGTH = 1;
    private static final String ALLOWED_CHARACTERS = "123456789";

    private static final SimpleCommandExceptionType ERROR_BLOCKED =
            new SimpleCommandExceptionType(MessageComponentSerializer.message().serialize(
                    Component.text("Invalid password. Choose a different password.")
            ));

    private static final SimpleCommandExceptionType ERROR_FORMAT =
            new SimpleCommandExceptionType(MessageComponentSerializer.message().serialize(
                    Component.text("Invalid password. It must match the configured length and contain only 1-9.")
            ));

    @Override
    public String parse(final StringReader reader) throws CommandSyntaxException {
        return parsePassword(reader);
    }

    @Override
    public <S> String parse(final StringReader reader, final S source) throws CommandSyntaxException {
        return parsePassword(reader);
    }

    private String parsePassword(final StringReader reader) throws CommandSyntaxException {
        final String password = reader.readUnquotedString();
        final int max = PasswordConfig.getPasswordLength();

        if (PasswordConfig.getBlockedPasswordList().contains(password)) {
            throw ERROR_BLOCKED.create();
        }
        if (!isValidPassword(password, max)) {
            throw ERROR_FORMAT.create();
        }
        return password;
    }

    private boolean isValidPassword(final String password, final int maxLen) {
        if (password.length() < MIN_LENGTH || password.length() > maxLen) return false;

        for (char c : password.toCharArray()) {
            if (ALLOWED_CHARACTERS.indexOf(c) == -1) return false;
        }
        return true;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
