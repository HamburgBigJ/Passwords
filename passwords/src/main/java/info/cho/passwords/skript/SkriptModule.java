package info.cho.passwords.skript;

import info.cho.passwords.utls.PLog;

public class SkriptModule {

    public static SkriptModule INSTANCE;

    public SkriptModule() {
        INSTANCE = this;
    }

    public void initSkriptModule() {
        PLog.debug("Init Skript");


    }

}
