package wtf.jukclient.module.modules.movement;

import wtf.jukclient.JukGlobals;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;

public class GuiMove extends Module {
    public GuiMove() {
        super("GuiMove", EnumCategory.Movement);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        JukGlobals.guiMove = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        JukGlobals.guiMove = false;
    }
}
