package wtf.jukclient.module;

import lombok.Getter;
import me.zero.alpine.listener.Subscribe;
import me.zero.alpine.listener.Subscriber;
import org.reflections.Reflections;
import wtf.jukclient.Juk;
import wtf.jukclient.event.KeyEvent;

import java.util.HashMap;
import java.util.Set;

public class ModuleManager implements Subscriber {
    @Getter
    private final HashMap<Class<? extends Module>, Module> modules = new HashMap<>();

    public ModuleManager() {
        register();
        Juk.getInstance().eventManager.subscribe(this);
    }

    public final Module getModule(Class<? extends Module> mod) {
        return modules.get(mod);
    }

    public final void register() {
        System.out.println("[juk] registering modules");
        final Reflections refl = new Reflections("wtf.jukclient.module.modules");
        final Set<Class<? extends Module>> classes = refl.getSubTypesOf(Module.class);

        for (Class<? extends Module> m : classes) {
            try {
                final Module inst = m.newInstance();
                modules.put(m, inst);
                System.out.println("[juk] registered module: " + m.getSimpleName());
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println("[juk] exception occurred while instantiating module " + m.getSimpleName());
            }
        }
    }

    @Subscribe
    private void onKey(KeyEvent event) {
        for (Module m : this.getModules().values()) {
            if (m.getKey() == event.key) {
                m.toggle();
            }
        }
    }
}