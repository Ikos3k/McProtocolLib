package me.ANONIMUS.mcprotocol.network.protocol.packet;

import me.ANONIMUS.mcprotocol.network.protocol.Protocol;
import me.ANONIMUS.mcprotocol.network.protocol.ProtocolType;

import java.util.Collections;
import java.util.List;

public abstract class Packet {
    public abstract void write(PacketBuffer out, int protocol);

    public abstract void read(PacketBuffer in, int protocol);

    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x00, ProtocolType.PROTOCOL_UNKNOWN));
    }
}