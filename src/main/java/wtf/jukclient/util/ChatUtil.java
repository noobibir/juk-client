package wtf.jukclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtil {
    public static void send(String msg, Object... args) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null) {
            return;
        }

        mc.thePlayer.addChatMessage(new ChatComponentText(String.format(msg, args)));
    }
}
