package info.cho.passwords.customGui;

import info.cho.passwords.events.PasswordClickEvent;
import info.cho.passwords.events.PasswordLoginFailEvent;
import info.cho.passwords.events.PasswordLoginSuccessEvent;
import info.cho.passwords.utls.PLog;
import info.cho.passwordsApi.password.PasswordConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CustomGui {

    public Map<String, Class<?>> customGuiList = new HashMap<>();

    public void registerGui(String name, Class<?> clazz) {
        if (customGuiList.containsKey(name)) {
            PLog.debug("Custom gui already registered: " + name);
            return;
        }
        customGuiList.put(name, clazz);
        PLog.debug("Custom gui registered: " + name);
    }

    public static void EventPasswordSuccess(Player player) {
        Bukkit.getPluginManager().callEvent(
                new PasswordLoginSuccessEvent(player, PasswordConfig.getSkiptBehaviour())
        );
    }

    public static void EventPasswordFail(Player player) {
        Bukkit.getPluginManager().callEvent(
                new PasswordLoginFailEvent(player, PasswordConfig.getSkiptBehaviour())
        );
    }

    public static void EventPasswordClick(Player player, int num) {
        Bukkit.getPluginManager().callEvent(
                new PasswordClickEvent(player, PasswordConfig.getSkiptBehaviour(), num)
        );
    }


}
