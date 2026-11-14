package wtf.jukclient.bot.net;

import com.mojang.authlib.GameProfile;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import wtf.jukclient.bot.BotData;
import wtf.jukclient.util.ChatUtil;

public class BotNetHandlerLogin implements INetHandlerLoginClient {
    private final BotData bot;
    private final BotNetManager networkManager;

    public BotNetHandlerLogin(BotNetManager networkManagerIn, BotData mcIn)
    {
        this.networkManager = networkManagerIn;
        this.bot = mcIn;
    }

    public void handleEncryptionRequest(S01PacketEncryptionRequest packetIn)
    {
        ChatUtil.send("encryption is not supported");
        networkManager.closeChannel(new ChatComponentText("encryption is not supported"));
    }

    public void handleLoginSuccess(S02PacketLoginSuccess packetIn)
    {
        GameProfile gameProfile = packetIn.getProfile();
        this.networkManager.setConnectionState(EnumConnectionState.PLAY);
        this.networkManager.setNetHandler(new BotNetHandlerPlay(this.bot, this.networkManager, gameProfile));
        this.bot.netHandler = this.networkManager.getNetHandler();
    }

    public void onDisconnect(IChatComponent reason)
    {
        ChatUtil.send("bot disconnected: %s", reason.getUnformattedTextForChat());
    }

    public void handleDisconnect(S00PacketDisconnect packetIn)
    {
        this.networkManager.closeChannel(packetIn.func_149603_c());
    }

    public void handleEnableCompression(S03PacketEnableCompression packetIn)
    {
        if (!this.networkManager.isLocalChannel())
        {
            this.networkManager.setCompressionTreshold(packetIn.getCompressionTreshold());
        }
    }
}
