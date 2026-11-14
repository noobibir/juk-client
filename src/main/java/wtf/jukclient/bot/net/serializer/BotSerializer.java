package wtf.jukclient.bot.net.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;

import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import wtf.jukclient.bot.net.BotNetManager;

public class BotSerializer extends MessageToByteEncoder<Packet> {
    private final EnumPacketDirection direction;

    public BotSerializer(EnumPacketDirection direction) {
        this.direction = direction;
    }

    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buf) throws Exception {
        Integer id = ctx.channel().attr(BotNetManager.attrKeyConnectionState).get().getPacketId(this.direction, packet);

        if (id == null) {
            throw new IOException("Can't serialize unregistered packet");
        }
        else {
            PacketBuffer packetbuffer = new PacketBuffer(buf);
            packetbuffer.writeVarIntToBuffer(id);

            try {
                packet.writePacketData(packetbuffer);
            }
            catch (Throwable throwable) {
                //logger.error((Object)throwable);
            }
        }
    }
}
