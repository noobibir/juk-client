package wtf.jukclient.module.setting;

import wtf.jukclient.ui.ClickGUI;

public class BoolSetting extends Setting {
    private boolean value = false;

    public BoolSetting(String name) {
        super(name);
    }

    @Override
    public void render(ClickGUI screen, float partialTicks, int mouseX, int mouseY, int x, int y) {
        screen.drawString(
                mc.fontRendererObj,
                name + (value ? " - enabled" : " - disabled"),
                x, y, 0xFFFFFFFF
        );
    }

    @Override
    public int getSettingHeight() {
        return mc.fontRendererObj.FONT_HEIGHT;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int x, int y) {
        mouseX -= x;
        mouseY -= y;
        if (mouseX >= 0 && mouseY >= 0 && mouseX <= mc.fontRendererObj.getStringWidth(name) && mouseY <= mc.fontRendererObj.FONT_HEIGHT) {
            this.value = !this.value;
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int x, int y) {
        return false;
    }
}
