package wtf.jukclient.module.setting;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import wtf.jukclient.ui.ClickGUI;

import java.util.function.BooleanSupplier;

public abstract class Setting {
    protected final Minecraft mc = Minecraft.getMinecraft();
    private BooleanSupplier settingRenderFunc;
    @Getter
    protected final String name;

    public Setting(String name) {
        this.name = name;
    }

    public abstract void render(ClickGUI screen, float partialTicks, int mouseX, int mouseY, int x, int y);
    public abstract int getSettingHeight();
    public abstract boolean mouseClicked(int mouseX, int mouseY, int x, int y);
    public abstract boolean mouseReleased(int mouseX, int mouseY, int x, int y);
    public boolean isRendered() {
        if (settingRenderFunc == null) {
            return true;
        }
        return settingRenderFunc.getAsBoolean();
    }
    public void addSettingRenderFunc (BooleanSupplier func) {
        settingRenderFunc = func;
    }
}
