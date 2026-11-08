package wtf.jukclient.module.setting;

import org.lwjgl.input.Keyboard;
import wtf.jukclient.JukGlobals;
import wtf.jukclient.ui.ClickGUI;
import wtf.jukclient.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

public class InputSetting extends Setting {
    private String value = "";
    private boolean selected = false;
    private final List<Character> allowedChars = new ArrayList<>();

    public InputSetting set(String value) {
        this.value = value;
        return this;
    }

    public InputSetting addAllowedChar(char c) {
        allowedChars.add(c);
        return this;
    }

    public String get() {
        return value;
    }

    public InputSetting(String name) {
        super(name);
    }

    @Override
    public void render(ClickGUI screen, float partialTicks, int mouseX, int mouseY, int x, int y) {
        screen.drawString(
                mc.fontRendererObj,
                this.name + ": ",
                x, y, JukGlobals.input_nameColor
        );
        int nameSize = mc.fontRendererObj.getStringWidth(this.name + ": ");
        int valueSize = mc.fontRendererObj.getStringWidth(this.value);
        screen.drawString(
                mc.fontRendererObj,
                this.value + (selected ? "|" : ""),
                x + nameSize,
                y, JukGlobals.input_nameColor
        );
        RenderUtil.line(x + nameSize,
                y + mc.fontRendererObj.FONT_HEIGHT,
                x + nameSize + valueSize,
                y + mc.fontRendererObj.FONT_HEIGHT,
                JukGlobals.input_nameColor
        );
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (!selected) {
            return;
        }
        if (keyCode == Keyboard.KEY_BACK) {
            if (!this.value.isEmpty()) {
                value = value.substring(0, value.length() - 1);
            }
        } else if (keyCode == Keyboard.KEY_RETURN) {
            selected = false;
        } else if (isValidChar(typedChar)) {
            value += typedChar;
        }
    }

    private boolean isValidChar(char c) {
        return allowedChars.contains(c);
    }

    @Override
    public int getSettingHeight() {
        return mc.fontRendererObj.FONT_HEIGHT;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int x, int y) {
        int size = mc.fontRendererObj.getStringWidth(this.name + ": " + (this.value.isEmpty() ? "_" : this.value));
        if (mouseX > x && mouseY > y && mouseX < size + x && mouseY < mc.fontRendererObj.FONT_HEIGHT + y) {
            selected = true;
            return false;
        } else {
            selected = false;
            return true;
        }
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int x, int y) {
        return false;
    }
}
