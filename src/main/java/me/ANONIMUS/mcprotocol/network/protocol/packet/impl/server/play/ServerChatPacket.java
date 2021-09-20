package me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.mcprotocol.network.protocol.Protocol;
import me.ANONIMUS.mcprotocol.network.protocol.data.MessagePosition;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketBuffer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerChatPacket extends Packet {
    private String message;
    private MessagePosition position;

    public ServerChatPacket(MessagePosition messagePosition, BaseComponent... message) {
        this(ComponentSerializer.toString(message), messagePosition);
    }

    public ServerChatPacket(BaseComponent... message) {
        this(MessagePosition.CHATBOX, message);
    }

    public ServerChatPacket(String message) {
        this(ComponentSerializer.toString(message), MessagePosition.CHATBOX);
    }

    @Override
    public void write(PacketBuffer out, int protocol) {
        out.writeString(message);
        out.writeByte(position.getId());
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        this.message = in.readString();
        this.position = MessagePosition.getById(in.readByte());
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x02, 47), new Protocol(0x0F, 107, 108, 109, 110, 210, 315, 316, 335, 338, 340));
    }
}