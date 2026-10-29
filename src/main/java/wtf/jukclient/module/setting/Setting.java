package wtf.jukclient.module.setting;

import java.util.function.BooleanSupplier;

public abstract class Setting {
    private BooleanSupplier settingRenderFunc;

    public abstract void render();
    public abstract int getSettingHeight();
    public abstract boolean mouseClicked(int mouseX, int mouseY);
    public abstract boolean mouseReleased(int mouseX, int mouseY);
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
