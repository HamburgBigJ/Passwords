package info.cho.passwords;

import info.cho.passwords.commands.LogoutPlayerCommand;
import info.cho.passwords.commands.SetPasswordCommand;
import info.cho.passwords.commands.SetPlayerPasswordCommand;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

public class PasswordsBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            var register = commands.registrar();

            register.register(SetPlayerPasswordCommand.build());
            register.register(SetPasswordCommand.build());
            register.register(LogoutPlayerCommand.build());


        });
    }
}
