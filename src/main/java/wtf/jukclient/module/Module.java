package wtf.jukclient.module;

import lombok.Getter;
import lombok.Setter;
import me.zero.alpine.listener.Subscriber;
import net.minecraft.client.Minecraft;
import wtf.jukclient.Juk;
import wtf.jukclient.module.setting.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module implements Subscriber {
    protected final Minecraft mc;
    @Getter
    private final String name;
    @Getter
    private final EnumCategory category;
    @Getter
    private boolean enabled;
    @Getter
    @Setter
    private int key;
    private final List<Setting> settings = new ArrayList<>();

    protected Module(String name, EnumCategory category) {
        this.name = name;
        this.category = category;
        mc = Minecraft.getMinecraft();

        // auto register all settings
        Arrays.stream(getClass().getFields()).forEach((field) -> {
            if (!field.getType().isInstance(Setting.class)) return;

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            try {
                settings.add((Setting) field.get(this));
            } catch (IllegalAccessException e) {
                System.out.println("[module] failed to register setting " + field.getName());
            }
        });
    }

    public void onEnable() {
        enabled = true;
        Juk.getInstance().eventManager.subscribe(this);
    }

    public void onDisable() {
        enabled = false;
        Juk.getInstance().eventManager.unsubscribe(this);
    }

    public void toggle() {
        if (enabled) {
            onDisable();
        } else {
            onEnable();
        }
    }
}
