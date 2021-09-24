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
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerChatPacket extends Packet {
    private String message;
    private MessagePosition position;
    private UUID sender;

    public ServerChatPacket(MessagePosition messagePosition, BaseComponent... message) {
        this(ComponentSerializer.toString(message), messagePosition, new UUID(0, 0));
    }

    public ServerChatPacket(BaseComponent... message) {
        this(MessagePosition.CHATBOX, message);
    }

    public ServerChatPacket(String message) {
        this(ComponentSerializer.toString(message), MessagePosition.CHATBOX, new UUID(0, 0));
    }

    public ServerChatPacket(String message, UUID sender) {
        this(ComponentSerializer.toString(message), MessagePosition.CHATBOX, sender);
    }

    @Override
    public void write(PacketBuffer out, int protocol) {
        out.writeString(message);
        out.writeByte(position.ordinal());
        if(protocol >= 735) {
            out.writeUuid(sender);
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        this.message = in.readString();
        this.position = MessagePosition.values()[in.readByte()];
        if(protocol >= 735) {
            this.sender = in.readUuid();
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x02, 47), new Protocol(0x0F, 107, 108, 109, 110, 210, 315, 316, 335, 338, 340, 393, 573, 735, 756));
    }
}