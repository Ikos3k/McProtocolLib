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
@AllArgsConstructor
@NoArgsConstructor
public class ServerCustomPayloadPacket extends Packet {
    private String channel;
    private PacketBuffer data;

    @Override
    public void write(PacketBuffer out, int protocol) {
        out.writeString(this.channel);
        out.writeBytes(this.data.getByteBuf());
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        this.channel = in.readString();
        this.data = new PacketBuffer(in.readBytes());
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x3F, 47, 110), new Protocol(0x18, 340));
    }
}