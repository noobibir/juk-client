package wtf.jukclient;

import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import wtf.jukclient.modules.ModuleManager;

public class Juk {
    private static Juk instance;

    public final ModuleManager moduleManager;
    public final EventBus eventManager = EventManager.builder()
            .setName("root/juk")
            .setSuperListeners()
            .build();

    public Juk() {
        moduleManager = new ModuleManager();
    }

    public static Juk getInstance() {
        if (instance == null) {
            instance = new Juk();
        }

        return instance;
    }
}
