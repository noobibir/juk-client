package wtf.jukclient.module.modules.visual;

import wtf.jukclient.JukGlobals;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;

public class CameraClip extends Module {
    public CameraClip() {
        super("CameraClip", EnumCategory.Visual);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        JukGlobals.cameraClip = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        JukGlobals.cameraClip = false;
    }
}
