package me.ANONIMUS.mcprotocol.network.protocol.packet.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketBuffer;

@Getter
@AllArgsConstructor
public class CustomPacket extends Packet {
    private int customPacketID;
    private byte[] customData;

    public CustomPacket(int id) {
        this.customPacketID = id;
    }

    public void write(PacketBuffer out, int protocol) {
        out.writeBytes(customData);
    }

    public void read(PacketBuffer in, int protocol) {
        this.customData = in.readByteArray();
    }
}