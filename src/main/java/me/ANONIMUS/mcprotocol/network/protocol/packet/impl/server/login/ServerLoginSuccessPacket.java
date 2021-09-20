package me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.mcprotocol.network.protocol.Protocol;
import me.ANONIMUS.mcprotocol.network.protocol.ProtocolType;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketBuffer;
import me.ANONIMUS.mcprotocol.objects.GameProfile;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerLoginSuccessPacket extends Packet {
    private UUID uuid;
    private String username;

    @Override
    public void write(PacketBuffer out, int protocol) {
        out.writeString(this.uuid == null ? "" : this.uuid.toString());
        out.writeString(this.username);
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        this.uuid = UUID.fromString(in.readString());
        this.username = in.readString();
    }

    public GameProfile getGameProfile() {
        return new GameProfile(uuid, username);
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x02, ProtocolType.PROTOCOL_UNKNOWN));
    }
}