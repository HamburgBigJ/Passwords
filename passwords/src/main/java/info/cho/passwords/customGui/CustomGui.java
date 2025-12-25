package info.cho.passwords.customGui;

import info.cho.passwords.Passwords;
import info.cho.passwords.utls.PLog;

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

}
