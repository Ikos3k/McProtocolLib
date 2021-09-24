package me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketBuffer;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientLoginStartPacket extends Packet {
    private String username;

    @Override
    public void write(PacketBuffer out, int protocol) {
        out.writeString(this.username);
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        this.username = in.readString(16);
    }
}