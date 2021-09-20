package me.ANONIMUS.mcprotocol.network.protocol.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.Getter;
import lombok.Setter;
import me.ANONIMUS.mcprotocol.network.protocol.Protocol;
import me.ANONIMUS.mcprotocol.network.protocol.data.ConnectionState;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketBuffer;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketDirection;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketRegistry;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.CustomPacket;

import java.util.List;

@Getter
@Setter
public class PacketCodec extends ByteToMessageCodec<Packet> {
    private final PacketDirection packetDirection;
    private ConnectionState connectionState;
    private int protocol;

    public PacketCodec(ConnectionState connectionState, PacketDirection packetDirection) {
        this.connectionState = connectionState;
        this.packetDirection = packetDirection;
    }

    @Override
    protected void encode(ChannelHandlerContext channel, Packet packet, ByteBuf byteBuf) {
        if (!byteBuf.isWritable()) return;

        final PacketBuffer packetbuffer = new PacketBuffer(byteBuf);

        if (packet instanceof CustomPacket) {
            packetbuffer.writeVarInt(((CustomPacket) packet).getCustomPacketID());
        } else {
            packetbuffer.writeVarInt(getPacketIDByProtocol(packet, protocol));
        }

        packet.write(packetbuffer, protocol);
    }

    @Override
    protected void decode(ChannelHandlerContext channel, ByteBuf byteBuf, List<Object> list) {
        if (!byteBuf.isReadable()) return;

        final PacketBuffer packetBuffer = new PacketBuffer(byteBuf);

        final int packetID = packetBuffer.readVarInt();

        Packet packet = PacketRegistry.createPacket(connectionState, packetDirection, packetID, protocol);

        packet.read(packetBuffer, protocol);

        list.add(packet);
        byteBuf.clear();
    }

    private int getPacketIDByProtocol(Packet packet, int protocol) {
        for (Protocol p : packet.getProtocolList()) {
            for (int protocol2 : p.getProtocols()) {
                if (protocol2 == protocol) {
                    return p.getId();
                }
            }
        }

        return packet.getProtocolList().get(0).getId();
    }
}