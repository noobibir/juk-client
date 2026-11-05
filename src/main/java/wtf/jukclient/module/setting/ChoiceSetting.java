package wtf.jukclient.module.setting;

import lombok.AllArgsConstructor;
import wtf.jukclient.JukGlobals;
import wtf.jukclient.ui.ClickGUI;

import java.util.ArrayList;
import java.util.List;

public class ChoiceSetting extends Setting {
    private String value;
    private final List<Choice> choices = new ArrayList<>();

    @AllArgsConstructor
    static class Choice {
        public String value;
        public int x, y;
        public int sizeX;
    }

    public ChoiceSetting(String name) {
        super(name);
    }

    public ChoiceSetting addOptions(String... options) {
        int x = mc.fontRendererObj.getStringWidth(this.name + ": ");
        int y = 0;
        int maxX = JukGlobals.clickgui_settingsWindowSizeX;
        int sepSize = mc.fontRendererObj.getStringWidth(", ");
        for (String opt : options) {
            int optSizeX = mc.fontRendererObj.getStringWidth(opt);
            if (x + sepSize + optSizeX >= maxX) {
                x = 10;
                y += mc.fontRendererObj.FONT_HEIGHT;
            }
            choices.add(new Choice(opt, x, y, optSizeX));
            x += sepSize + optSizeX;
        }

        return this;
    }

    public ChoiceSetting set(String value) {
        this.value = value;
        return this;
    }

    public String get() {
        return value;
    }

    @Override
    public void render(ClickGUI screen, float partialTicks, int mouseX, int mouseY, int x, int y) {
        mc.fontRendererObj.drawString(name + ": ", x, y, JukGlobals.choice_nameColor);
        List<Choice> choiceList = this.choices;
        for (int i = 0; i < choiceList.size(); i++) {
            Choice choice = choiceList.get(i);
            int col = JukGlobals.choice_valueColor;
            if (choice.value.equals(this.value)) {
                col = JukGlobals.choice_selectedValueColor;
            }
            mc.fontRendererObj.drawString(choice.value, choice.x + x, choice.y + y, col);
            if (i != choiceList.size() - 1) {
                mc.fontRendererObj.drawString(", ", choice.x + x + choice.sizeX, choice.y + y, 0xFFFFFFFF);
            }
        }
    }

    @Override
    public int getSettingHeight() {
        return this.choices.get(this.choices.size() - 1).y + mc.fontRendererObj.FONT_HEIGHT;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int x, int y) {
        mouseX -= x;
        mouseY -= y;
        for (Choice choice : this.choices) {
            if (mouseX >= choice.x && mouseY >= choice.y && mouseX <= choice.x + choice.sizeX && mouseY <= choice.y + mc.fontRendererObj.FONT_HEIGHT) {
                this.value = choice.value;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int x, int y) {
        return false;
    }
}
