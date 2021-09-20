package me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.mcprotocol.network.protocol.Protocol;
import me.ANONIMUS.mcprotocol.network.protocol.ProtocolType;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketBuffer;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientStatusPingPacket extends Packet {
    private long time;

    @Override
    public void write(PacketBuffer out, int protocol) {
        out.writeLong(this.time);
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        this.time = in.readLong();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x01, ProtocolType.PROTOCOL_UNKNOWN));
    }
}