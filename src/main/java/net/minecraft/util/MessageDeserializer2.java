package net.minecraft.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;
import net.minecraft.network.PacketBuffer;

public class MessageDeserializer2 extends ByteToMessageDecoder
{
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> p_decode_3_) {
        buf.markReaderIndex();
        byte[] abyte = new byte[3];

        for (int i = 0; i < abyte.length; ++i)
        {
            if (!buf.isReadable())
            {
                buf.resetReaderIndex();
                return;
            }

            abyte[i] = buf.readByte();

            if (abyte[i] >= 0)
            {
                PacketBuffer packetbuffer = new PacketBuffer(Unpooled.wrappedBuffer(abyte));

                try
                {
                    int j = packetbuffer.readVarIntFromBuffer();

                    if (buf.readableBytes() >= j)
                    {
                        p_decode_3_.add(buf.readBytes(j));
                        return;
                    }

                    buf.resetReaderIndex();
                }
                finally
                {
                    packetbuffer.release();
                }

                return;
            }
        }

        throw new CorruptedFrameException("length wider than 21-bit");
    }
}
