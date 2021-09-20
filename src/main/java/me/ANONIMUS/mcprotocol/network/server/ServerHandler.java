package me.ANONIMUS.mcprotocol.network.server;

import lombok.RequiredArgsConstructor;
import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;

@RequiredArgsConstructor
public abstract class ServerHandler {
    public final MinecraftServer server;

    public abstract void disconnect();

    public abstract void handlePacket(Session session, final Packet packet);
}