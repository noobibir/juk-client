package wtf.jukclient.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import wtf.jukclient.Juk;
import wtf.jukclient.JukGlobals;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;
import wtf.jukclient.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class ClickGuiFrame {
    private boolean dragged = false;
    private int dragOffsetX;
    private int dragOffsetY;
    private int dragPosX;
    private int dragPosY;
    private int posX;
    private int posY;
    private final int sizeX;
    private final EnumCategory category;
    private final ClickGUI owner;
    private final List<Module> modules = new ArrayList<>();
    private final FontRenderer fr;

    public ClickGuiFrame(EnumCategory category, ClickGUI owner, int x, int y) {
        this.category = category;
        this.owner = owner;
        this.fr = Minecraft.getMinecraft().fontRendererObj;

        for (Module m : Juk.getInstance().moduleManager.getModules().values()) {
            if (m.getCategory() == category) {
                this.modules.add(m);
            }
        }
        this.modules.sort((m1, m2) -> fr.getStringWidth(m2.getName()) - fr.getStringWidth(m1.getName()));
        this.sizeX = Math.max(
                fr.getStringWidth(category.name()),
                fr.getStringWidth(this.modules.get(0).getName())
        ) + 20;

        this.posX = x + sizeX / 2 + 10;
        this.posY = y;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        // render category
        int catLeft = posX - sizeX / 2;
        int catTop = posY;
        int catRight = posX + sizeX / 2;
        int catBottom = posY + fr.FONT_HEIGHT;
        int bgCol = (mouseX >= catLeft && mouseX <= catRight && mouseY >= catTop && mouseY <= catBottom) ?
                JukGlobals.clickguiframe_categoryBackgroundColor_hover : JukGlobals.clickguiframe_categoryBackgroundColor;
        ClickGUI.drawRect(catLeft, catTop, catRight, catBottom, bgCol);
        owner.drawCenteredString(fr, category.name(), posX, posY, JukGlobals.clickguiframe_categoryColor);
        // render modules
        int mListLeft = posX - sizeX / 2 + 5;
        int mListTop = posY + fr.FONT_HEIGHT;
        int mListRight = posX + sizeX / 2 - 5;
        int mListBottom = posY + fr.FONT_HEIGHT * (1 + modules.size());

        ClickGUI.drawRect(mListLeft, mListTop, mListRight, mListBottom, JukGlobals.clickguiframe_modulesBackgroundColor);
        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            int mTop = posY + fr.FONT_HEIGHT * (1 + i);
            int mBottom = posY + fr.FONT_HEIGHT * (2 + i);
            boolean hovered = mouseX >= mListLeft && mouseX <= mListRight && mouseY > mTop && mouseY <= mBottom;
            boolean enabled = m.isEnabled();
            int mCol = hovered ?
                    (enabled ? JukGlobals.clickguiframe_moduleEnabledColor_hover : JukGlobals.clickguiframe_moduleDisabledColor_hover) :
                    (enabled ? JukGlobals.clickguiframe_moduleEnabledColor : JukGlobals.clickguiframe_moduleDisabledColor);
            owner.drawCenteredString(fr, m.getName(), posX, mTop, mCol);
        }

        //drag update
        if (dragged) {
            dragPosX = mouseX - dragOffsetX;
            dragPosY = mouseY - dragOffsetY;

            outline(
                    dragPosX - sizeX / 2,
                    dragPosY,
                    dragPosX + sizeX / 2,
                    dragPosY + fr.FONT_HEIGHT,
                    0xFFFFFFFF
            );
        }
    }

    public boolean mouseClick(int mouseX, int mouseY, int mouseButton) {
        //begin dragging
        int catLeft = posX - sizeX / 2;
        int catTop = posY;
        int catRight = posX + sizeX / 2;
        int catBottom = posY + fr.FONT_HEIGHT;
        if (mouseX >= catLeft && mouseY >= catTop && mouseX <= catRight && mouseY <= catBottom) {
            dragged = true;
            dragOffsetX = mouseX - posX;
            dragOffsetY = mouseY - posY;
            dragPosX = posX;
            dragPosY = posY;
            return true;
        }

        int mListLeft = posX - sizeX / 2 + 5;
        int mListRight = posX + sizeX / 2 - 5;
        if (mouseX >= mListLeft && mouseX <= mListRight) {
            int mInd = (mouseY - posY) / fr.FONT_HEIGHT - 1;
            if (mInd >= 0 && mInd < modules.size()) {
                if (mouseButton == 0) {
                    modules.get(mInd).toggle();
                }
                else if (mouseButton == 1) {
                    ChatUtil.send("open settings");
                }
            }
            return true;
        }

        return false;
    }

    public void mouseRelease(int mouseX, int mouseY) {
        if (dragged) {
            dragged = false;
            posX = dragPosX;
            posY = dragPosY;
        }
    }

    private void outline(int left, int top, int right, int bottom, int color) {
        if (left < right) {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);
        worldrenderer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
