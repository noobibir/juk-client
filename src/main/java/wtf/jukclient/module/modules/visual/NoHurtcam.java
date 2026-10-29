package wtf.jukclient.module.modules.visual;

import wtf.jukclient.JukGlobals;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;

public class NoHurtcam extends Module {
    public NoHurtcam() {
        super("NoHurtcam", EnumCategory.Visual);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        JukGlobals.noHurtCam = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        JukGlobals.noHurtCam = false;
    }
}
