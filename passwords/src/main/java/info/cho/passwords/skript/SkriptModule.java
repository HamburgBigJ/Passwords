package info.cho.passwords.skript;

import ch.njol.skript.Skript;
import info.cho.passwords.Passwords;
import info.cho.passwords.events.PasswordClickEvent;
import info.cho.passwords.events.PasswordLoginFailEvent;
import info.cho.passwords.events.PasswordLoginSuccessEvent;
import info.cho.passwords.utls.PLog;
import org.bukkit.entity.Player;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;

public class SkriptModule {

    public static SkriptModule INSTANCE;

    private final SkriptAddon skAddon;

    public SkriptModule() {
        this.skAddon = Skript.instance().registerAddon(Passwords.instance.getClass(), "Passwords");
        INSTANCE = this;
    }

    public void initSkriptModule() {
        PLog.debug("Init Skript");

    }

    private void register() {
        EventValueRegistry eventValues = skAddon.registry(EventValueRegistry.class);

        /*eventValues.register(
                PasswordLoginSuccessEvent.class,
                Player.class,
                PasswordLoginSuccessEvent::getPlayer,
                0
        );

        eventValues.register(
                PasswordLoginFailEvent.class,
                Player.class,
                PasswordLoginFailEvent::getPlayer,
                0
        );

        eventValues.register(
                PasswordClickEvent.class,
                Player.class,
                PasswordClickEvent::getPlayer,
                0
        );

        eventValues.register(
                PasswordClickEvent.class,
                Number.class,
                PasswordClickEvent::getNumber,
                0
        );

        eventValues.register(
                PasswordLoginSuccessEvent.class,
                String.class,
                PasswordLoginSuccessEvent::getBehaviorName,
                0
        );

        eventValues.register(
                PasswordLoginFailEvent.class,
                String.class,
                PasswordLoginFailEvent::getBehaviorName,
                0
        );

        eventValues.register(
                PasswordClickEvent.class,
                String.class,
                PasswordClickEvent::getBehaviorName,
                0
        );

         */
    }

}

