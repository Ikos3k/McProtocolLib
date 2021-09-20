package me.ANONIMUS.mcprotocol.network.client;

import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;

public interface ClientListener {
    void connected(Session session);

    void disconnected(Session session, String reason, Throwable cause);

    void packetReceived(Session session, Packet packet);
}