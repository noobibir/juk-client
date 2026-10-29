package wtf.jukclient;

import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import me.zero.alpine.listener.Subscribe;
import me.zero.alpine.listener.Subscriber;
import wtf.jukclient.event.KeyEvent;
import wtf.jukclient.module.Module;
import wtf.jukclient.module.ModuleManager;

public class Juk implements Subscriber {
    private static Juk instance;

    public final ModuleManager moduleManager;
    public final EventBus eventManager = EventManager.builder()
            .setName("root/juk")
            .setSuperListeners()
            .build();

    public Juk() {
        moduleManager = new ModuleManager();
        eventManager.subscribe(this);
    }

    public static Juk getInstance() {
        if (instance == null) {
            instance = new Juk();
        }

        return instance;
    }

    @Subscribe
    private void onKey(KeyEvent event) {
        for (Module m : moduleManager.getModules().values()) {
            if (m.getKey() == event.key) {
                m.toggle();
            }
        }
    }
}
