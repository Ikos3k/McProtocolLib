package me.ANONIMUS.mcprotocol.network.protocol.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.CorruptedFrameException;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketBuffer;

import java.util.List;
import java.util.stream.IntStream;

public class VarInt21FrameCodec extends ByteToMessageCodec<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) {
        final int size = byteBuf.readableBytes();
        final int j = IntStream.range(1, 5)
                .filter(i -> (size & -1 << i * 7) == 0)
                .findFirst()
                .orElse(5);

        if (j > 3) {
            throw new IllegalArgumentException("unable to fit " + size + " into 3");
        }

        final PacketBuffer packetbuffer = new PacketBuffer(byteBuf2);
        byteBuf2.ensureWritable(j + size);
        packetbuffer.writeVarInt(size);
        byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), size);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        byteBuf.markReaderIndex();
        final byte[] bytes = new byte[3];

        for (int i = 0; i < bytes.length; ++i) {
            if (!byteBuf.isReadable()) {
                byteBuf.resetReaderIndex();
                return;
            }

            bytes[i] = byteBuf.readByte();

            if (bytes[i] >= 0) {
                final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.wrappedBuffer(bytes));
                try {
                    final int j = packetbuffer.readVarInt();
                    if (byteBuf.readableBytes() >= j) {
                        list.add(byteBuf.readBytes(j));
                        return;
                    }

                    byteBuf.resetReaderIndex();
                } finally {
                    packetbuffer.release();
                }
                return;
            }
        }

        throw new CorruptedFrameException("length wider than 21-bit");
    }
}