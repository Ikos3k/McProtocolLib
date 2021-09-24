package me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.mcprotocol.network.protocol.ProtocolType;
import me.ANONIMUS.mcprotocol.network.protocol.data.HandshakeIntent;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketBuffer;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HandshakePacket extends Packet {
    private int protocolId;
    private String host;
    private int port;
    private HandshakeIntent intent;

    public HandshakePacket(ProtocolType protocol, String host, int port, HandshakeIntent intent) {
        this(protocol.getProtocol(), host, port, intent);
    }

    @Override
    public void write(PacketBuffer out, int protocol) {
        out.writeVarInt(this.protocolId);
        out.writeString(this.host);
        out.writeShort(this.port);
        out.writeVarInt(this.intent.ordinal() + 1);
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        this.protocolId = in.readVarInt();
        this.host = in.readString();
        this.port = in.readShort();
        this.intent = HandshakeIntent.values()[in.readVarInt() - 1];
    }
}