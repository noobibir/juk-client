package wtf.jukclient.ui;

import net.minecraft.client.gui.GuiScreen;
import wtf.jukclient.module.EnumCategory;

import java.io.IOException;

public class ClickGUI extends GuiScreen {
    private final ClickGuiFrame[] frames;

    public ClickGUI() {
        EnumCategory[] categories = EnumCategory.values();
        frames = new ClickGuiFrame[categories.length];
        frames[0] = new ClickGuiFrame(EnumCategory.Combat, this, 20, 20);
        frames[1] = new ClickGuiFrame(EnumCategory.Movement, this, 100, 20);
        frames[2] = new ClickGuiFrame(EnumCategory.Exploit, this, 180, 20);
        frames[3] = new ClickGuiFrame(EnumCategory.Misc, this, 260, 20);
        frames[4] = new ClickGuiFrame(EnumCategory.Player, this, 340, 20);
        frames[5] = new ClickGuiFrame(EnumCategory.Visual, this, 420, 20);
        frames[6] = new ClickGuiFrame(EnumCategory.World, this, 500, 20);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (ClickGuiFrame frame : frames) {
            frame.draw(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (ClickGuiFrame frame : frames) {
            if (frame.mouseClick(mouseX, mouseY, mouseButton)) {
                return;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (ClickGuiFrame frame : frames) {
            frame.mouseRelease(mouseX, mouseY);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawBackground(int tint) {

    }
}
