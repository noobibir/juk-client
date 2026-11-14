package wtf.jukclient.bot.net.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.IOException;
import java.util.List;

import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import wtf.jukclient.bot.net.BotNetManager;

public class BotDeserializer extends ByteToMessageDecoder {
    private final EnumPacketDirection direction;

    public BotDeserializer(EnumPacketDirection direction) {
        this.direction = direction;
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        if (buf.readableBytes() != 0) {
            PacketBuffer packetbuffer = new PacketBuffer(buf);
            int i = packetbuffer.readVarIntFromBuffer();
            Packet packet = ctx.channel().attr(BotNetManager.attrKeyConnectionState).get().getPacket(this.direction, i);

            if (packet == null) {
                throw new IOException("Bad packet id " + i);
            }
            else {
                packet.readPacketData(packetbuffer);

                if (packetbuffer.readableBytes() > 0) {
                    throw new IOException("Packet " + ctx.channel().attr(BotNetManager.attrKeyConnectionState).get().getId() + "/" + i + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + packetbuffer.readableBytes() + " bytes extra whilst reading packet " + i);
                }
                else {
                    out.add(packet);
                }
            }
        }
    }
}
