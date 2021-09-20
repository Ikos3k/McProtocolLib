package me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketBuffer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerLoginDisconnectPacket extends Packet {
    private BaseComponent[] reason;

    public ServerLoginDisconnectPacket(String message) {
        this(new ComponentBuilder(message).create());
    }

    @Override
    public void write(PacketBuffer out, int protocol) {
        out.writeChatComponent(this.reason);
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        this.reason = in.readChatComponent();
    }
}