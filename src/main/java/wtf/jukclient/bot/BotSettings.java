package wtf.jukclient.bot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.world.EnumDifficulty;

public class BotSettings {
    public boolean keyBindForward = false;
    public boolean keyBindLeft = false;
    public boolean keyBindBack = false;
    public boolean keyBindRight = false;
    public boolean keyBindJump = false;
    public boolean keyBindSneak = false;
    public boolean keyBindSprint = false;
    public boolean keyBindUseItem = false;

    public static float mouseSensitivity = 0.5F;
    public static int renderDistanceChunks = -1;
    public static boolean useNativeTransport = true;
    public static String language = "zh_CN"; // kitayski juk
    public EnumDifficulty difficulty;

    protected BotData bot;

    public BotSettings(BotData botData) {
        this.bot = botData;
    }

    public void sendSettingsToServer() {
        if (bot.player != null) {
            bot.player.sendQueue.addToSendQueue(new C15PacketClientSettings(
                    language, renderDistanceChunks, EntityPlayer.EnumChatVisibility.FULL, true, 0));
        }
    }
}
