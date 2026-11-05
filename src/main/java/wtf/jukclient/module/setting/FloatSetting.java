package wtf.jukclient.module.setting;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import wtf.jukclient.JukGlobals;
import wtf.jukclient.ui.ClickGUI;
import wtf.jukclient.util.RenderUtil;

public class FloatSetting extends Setting {
    private float value;
    private float minValue = Float.MIN_VALUE;
    private float maxValue = Float.MAX_VALUE;
    private boolean held = false;

    public FloatSetting(String name) {
        super(name);
    }

    public FloatSetting bound(float min, float max) {
        this.minValue = min;
        this.maxValue = max;
        return this;
    }

    public FloatSetting set(float value) {
        this.value = value;
        return this;
    }

    public float get() {
        return value;
    }

    @Override
    public void render(ClickGUI screen, float partialTicks, int mouseX, int mouseY, int x, int y) {
        screen.drawString(mc.fontRendererObj, name, x, y, JukGlobals.slider_nameColor);
        x += mc.fontRendererObj.getStringWidth(name) + 5;
        float fill = MathHelper.clamp_float((float) (value - minValue) / (maxValue - minValue), 0, 1);
        ClickGUI.drawRect(x, y + 1, x + (int) (fill * JukGlobals.slider_width), y + mc.fontRendererObj.FONT_HEIGHT - 1, JukGlobals.slider_fillColor);
        RenderUtil.outline(x, y + 1, x + JukGlobals.slider_width, y + mc.fontRendererObj.FONT_HEIGHT - 1, JukGlobals.slider_outlineColor);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + JukGlobals.slider_width / 2.0f, y + mc.fontRendererObj.FONT_HEIGHT / 2.0f, 0);
        GlStateManager.scale(1f, 0.7f, 1);
        boolean hovered = (mouseX >= x && mouseX <= x + JukGlobals.slider_width && mouseY > y && mouseY < y + mc.fontRendererObj.FONT_HEIGHT);
        int valCol = (hovered || held) ? JukGlobals.slider_valueColor_hover : JukGlobals.slider_valueColor;
        screen.drawCenteredString(mc.fontRendererObj, value + "", 0, -mc.fontRendererObj.FONT_HEIGHT / 2, valCol);
        GlStateManager.popMatrix();

        if (held) {
            mouseX -= x;
            value = round((mouseX / (float) JukGlobals.slider_width) * (maxValue - minValue) + minValue);
            value = MathHelper.clamp_float(value, minValue, maxValue);
        }
    }

    private float round(float value) {
        return (int) (value * 1000) / 1000.0f;
    }

    @Override
    public int getSettingHeight() {
        return mc.fontRendererObj.FONT_HEIGHT;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int x, int y) {
        mouseX -= x + mc.fontRendererObj.getStringWidth(name) + 5;
        mouseY -= y;
        if (mouseX < 0 || mouseY <= 0 || mouseX > JukGlobals.slider_width || mouseY >= mc.fontRendererObj.FONT_HEIGHT) {
            return false;
        }

        held = true;
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int x, int y) {
        if (held) {
            held = false;
            return true;
        }
        return false;
    }
}
