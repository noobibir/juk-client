package wtf.jukclient.bot;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.Session;
import wtf.jukclient.util.ChatUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class BotHelper {
    public static BotData createBot(String ip, int port, String username) {
        ChatUtil.send("connecting to %s:%d", ip, port);
        BotData bot = new BotData();

        InetAddress address;
        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            ChatUtil.send("failed to connect");
            return null;
        }
        bot.session = new Session(username, username, "0", "MOJANG");
        bot.netManager = NetworkManager.createNetworkManagerAndConnect(address, port, false);
        bot.netHandler = new BotNetHandlerLogin(bot.netManager, bot);
        bot.netManager.setNetHandler(bot.netHandler);
        bot.netManager.sendPacket(new C00Handshake(47, ip, port, EnumConnectionState.LOGIN));
        bot.netManager.sendPacket(new C00PacketLoginStart(bot.session.getProfile()));

        return bot;
    }
}
