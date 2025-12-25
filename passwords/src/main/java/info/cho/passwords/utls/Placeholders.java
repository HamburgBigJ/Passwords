package info.cho.passwords.utls;

import info.cho.passwordsApi.password.PasswordConfig;
import info.cho.passwordsApi.password.customgui.PasswordsGui;
import info.cho.passwordsApi.placeholders.CustomPlaceholder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Placeholders {

    public List<Class<? extends CustomPlaceholder>> placeholders = new ArrayList<>();

    public void registerPlaceholder(Class<? extends CustomPlaceholder> clazz) {
        placeholders.add(clazz);
    }

    public String applyPlaceholder(String message, Player player) {
        String s = message;

        for (Class<? extends CustomPlaceholder> aClass : placeholders) {
            try {
                CustomPlaceholder clazz = aClass.getDeclaredConstructor().newInstance();
                s = s.replace(clazz.keyId(), clazz.resultKey(player));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return s;

    }

}
