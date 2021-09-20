package me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.mcprotocol.network.protocol.Protocol;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketBuffer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerDisconnectPacket extends Packet {
    private BaseComponent[] reason;

    public ServerDisconnectPacket(String message) {
        this(new ComponentBuilder(message).create());
    }

    @Override
    public void write(PacketBuffer out, int protocol) {
        out.writeChatComponent(reason);
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        this.reason = in.readChatComponent();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x40, 47), new Protocol(0x1A, 107, 108, 110, 315, 316, 335, 338, 340));
    }
}