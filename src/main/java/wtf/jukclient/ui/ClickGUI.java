package wtf.jukclient.ui;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import wtf.jukclient.JukGlobals;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;
import wtf.jukclient.module.setting.Setting;
import wtf.jukclient.util.RenderUtil;

import java.io.IOException;

public class ClickGUI extends GuiScreen {
    private final ClickGuiFrame[] frames;
    private Module ms = null;
    private final int settingWindowSizeX = 16 * 30;
    private final int settingWindowSizeY = 9 * 30;
    private int settingWindowX;
    private int settingWindowY;
    private boolean isAwaitingBind = false;
    private boolean draggingSettings = false;
    private int dragOffsetX;
    private int dragOffsetY;

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
    public void initGui() {
        settingWindowX = this.width / 2 - settingWindowSizeX / 2;
        settingWindowY = this.height / 2 - settingWindowSizeY / 2;
        isAwaitingBind = false;
        ms = null;
    }

    public void openSettings(Module m) {
        this.ms = m;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (ClickGuiFrame frame : frames) {
            frame.draw(mouseX, mouseY, partialTicks);
        }

        if (this.ms != null) {
            if (draggingSettings) {
                settingWindowX = mouseX - dragOffsetX;
                settingWindowY = mouseY - dragOffsetY;
            }

            drawRect(0, 0, this.width, this.height, JukGlobals.clickgui_settingsWindowTint); //background tint
            drawRect( //more tint on window
                    settingWindowX, settingWindowY,
                    settingWindowX + settingWindowSizeX,
                    settingWindowY + settingWindowSizeY,
                    JukGlobals.clickgui_settingsWindowBackgroundTint
            );
            RenderUtil.outline( // window borders
                    settingWindowX,
                    settingWindowY,
                    settingWindowX + settingWindowSizeX,
                    settingWindowY + settingWindowSizeY,
                    JukGlobals.clickgui_settingsWindowBorder
            );
            String bind = isAwaitingBind ? "..." : Keyboard.getKeyName(ms.getKey());
            String title = ms.getName() + " - bind: " + bind // window title
                    + " - " + (ms.isEnabled() ? "enabled" : "disabled");
            fontRendererObj.drawString(title, settingWindowX + 1, settingWindowY + 1, JukGlobals.clickgui_settingsWindowTitle);
            // close button
            int cright = settingWindowX + settingWindowSizeX - 1;
            int cleft = cright - 20;
            int ctop = settingWindowY + 1;
            int cbottom = ctop + 10;
            boolean hovered = mouseX >= cleft && mouseX <= cright && mouseY >= ctop && mouseY <= cbottom;
            int buttonColor = hovered ? JukGlobals.clickgui_settingsWindowClose_hover : JukGlobals.clickgui_settingsWindowClose;
            if (hovered) {
                drawRect(cleft, ctop, cright, cbottom, JukGlobals.clickgui_settingsWindowCloseBackground);
            }
            RenderUtil.line(cleft + 7, ctop + 2, cright - 7, cbottom - 2, buttonColor);
            RenderUtil.line(cleft + 7, cbottom - 2, cright - 7, ctop + 2, buttonColor);
            // line between window title and settings
            drawHorizontalLine(settingWindowX, settingWindowX + settingWindowSizeX - 1,
                    settingWindowY + fontRendererObj.FONT_HEIGHT + 2, JukGlobals.clickgui_settingsWindowBorder);
            int sy = settingWindowY + fontRendererObj.FONT_HEIGHT + 4;
            for (Setting s : ms.getSettings()) {
                if (s.isRendered()) {
                    s.render(this, partialTicks, mouseX, mouseY, settingWindowX + 3, sy);
                    sy += s.getSettingHeight();
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (isAwaitingBind && this.ms != null) {
            this.ms.setKey(keyCode);
            isAwaitingBind = false;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isAwaitingBind) {
            isAwaitingBind = false;
        }

        if (this.ms != null) {
            int sy = settingWindowY + fontRendererObj.FONT_HEIGHT + 4;

            if (mouseY <= sy && mouseY >= settingWindowY) {
                //binding
                String t1 = ms.getName() + " - bind:";
                String bind = Keyboard.getKeyName(ms.getKey());
                String t2 = ms.getName() + " - bind: " + bind;
                int t1size = fontRendererObj.getStringWidth(t1);
                int t2size = fontRendererObj.getStringWidth(t2);
                // toggle module
                String t3 = ms.getName() + " - bind: " + bind + " - ";
                String t4 = ms.getName() + " - bind: " + bind + " - " + (ms.isEnabled() ? "enabled" : "disabled");
                int t3size = fontRendererObj.getStringWidth(t3);
                int t4size = fontRendererObj.getStringWidth(t4);
                // close button
                int cright = settingWindowX + settingWindowSizeX - 1;
                int cleft = cright - 20;
                if (mouseX >= settingWindowX + t1size && mouseX <= settingWindowX + t2size) {
                    isAwaitingBind = true;
                }
                else if (mouseX >= settingWindowX + t3size && mouseX <= settingWindowX + t4size) {
                    ms.toggle();
                }
                else if (mouseX >= cleft && mouseX <= cright) {
                    this.ms = null;
                    return;
                }
                else {
                    draggingSettings = true;
                    dragOffsetX = mouseX - settingWindowX;
                    dragOffsetY = mouseY - settingWindowY;
                }
            }

            for (Setting s : ms.getSettings()) {
                if (s.isRendered()) {
                    boolean changed = s.mouseClicked(mouseX, mouseY, settingWindowX + 3, sy);
                    sy += s.getSettingHeight();

                    if (changed) {
                        s.onChanged();
                    }
                }
            }
            return;
        }

        for (ClickGuiFrame frame : frames) {
            if (frame.mouseClick(mouseX, mouseY, mouseButton)) {
                return;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.ms != null) {
            draggingSettings = false;
            return;
        }

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
