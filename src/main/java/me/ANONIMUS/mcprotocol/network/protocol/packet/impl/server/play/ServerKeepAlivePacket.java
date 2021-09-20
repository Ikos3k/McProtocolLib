package me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.mcprotocol.network.protocol.Protocol;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerKeepAlivePacket extends Packet {
    private long keepaliveId;

    @Override
    public void write(PacketBuffer out, int protocol) {
        if (protocol == 5) {
            out.writeInt((int) this.keepaliveId);
        } else if (protocol >= 340) {
            out.writeLong(this.keepaliveId);
        } else {
            out.writeVarInt((int) this.keepaliveId);
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        if (protocol == 5) {
            this.keepaliveId = in.readInt();
        } else if (protocol >= 340) {
            this.keepaliveId = in.readLong();
        } else {
            this.keepaliveId = in.readVarInt();
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x00, 5, 47), new Protocol(0x1F, 107, 108, 109, 110, 210, 315, 316, 335, 338, 340));
    }
}