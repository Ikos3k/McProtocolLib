package me.ANONIMUS.mcprotocol.network.server;

import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;

public interface ServerListener {
    void serverClosed();

    void sessionAdded(Session session);

    void sessionRemoved(Session session);

    void packetReceived(Session session, Packet packet);
}