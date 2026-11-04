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
    @Getter
    private final List<Setting> settings = new ArrayList<>();

    protected Module(String name, EnumCategory category) {
        this.name = name;
        this.category = category;
        mc = Minecraft.getMinecraft();
    }

    public void onEnable() {
        enabled = true;
        Juk.getInstance().eventManager.subscribe(this);
    }

    public void onDisable() {
        enabled = false;
        Juk.getInstance().eventManager.unsubscribe(this);
    }

    protected <T extends Setting> T addSetting (T setting) {
        this.settings.add(setting);
        return setting;
    }

    public void toggle() {
        if (enabled) {
            onDisable();
        } else {
            onEnable();
        }
    }
}
