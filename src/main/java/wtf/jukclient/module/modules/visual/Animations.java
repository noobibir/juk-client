package wtf.jukclient.module.modules.visual;

import wtf.jukclient.JukGlobals;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;
import wtf.jukclient.module.setting.BoolSetting;

public class Animations extends Module {
    private final BoolSetting oddSwing = addSetting(new BoolSetting("oddSwing"));
    private final BoolSetting oneSevenBlock = addSetting(new BoolSetting("1.7 blocking"));

    public Animations() {
        super("Animations", EnumCategory.Visual);
        oddSwing.addSettingChangedFunc(() -> JukGlobals.oddSwing = oddSwing.get());
        oneSevenBlock.addSettingChangedFunc(() -> JukGlobals.oneSevenBlock = oneSevenBlock.get());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        JukGlobals.oddSwing = oddSwing.get();
        JukGlobals.oneSevenBlock = oneSevenBlock.get();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        JukGlobals.oddSwing = false;
        JukGlobals.oneSevenBlock = false;
    }
}
