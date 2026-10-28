package wtf.jukclient.modules;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;

public class ModuleManager {
    private final HashMap<Class<? extends Module>, Module> modules = new HashMap<>();

    public ModuleManager() {
        register();
    }

    public HashMap<Class<? extends Module>, Module> getModules() {
        return modules;
    }

    public final Module getModule(Class<? extends Module> mod) {
        return modules.get(mod);
    }

    public final void register() {
        final Reflections refl = new Reflections("wtf.jukclient.modules.impl");
        final Set<Class<? extends Module>> classes = refl.getSubTypesOf(Module.class);

        for (Class<? extends Module> m : classes) {
            try {
                final Module feat = m.newInstance();
                modules.put(m, feat);
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println("exception occurred while instantiating module " + m.getSimpleName());
            }
        }
    }
}