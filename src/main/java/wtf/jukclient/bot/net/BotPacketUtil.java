package wtf.jukclient.bot.net;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.util.IThreadListener;

public class BotPacketUtil {
    public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> packet, final T handler, IThreadListener listener) throws ThreadQuickExitException {
        if (!listener.isCallingFromMinecraftThread())
        {
            listener.addScheduledTask(() -> packet.processPacket(handler));
            throw ThreadQuickExitException.INSTANCE;
        }
    }
}
