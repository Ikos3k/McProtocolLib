package examples.bots;

import me.ANONIMUS.mcprotocol.MinecraftProtocol;
import me.ANONIMUS.mcprotocol.network.client.ClientListener;
import me.ANONIMUS.mcprotocol.network.client.MinecraftClient;
import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.ProtocolType;
import me.ANONIMUS.mcprotocol.network.protocol.data.ConnectionState;
import me.ANONIMUS.mcprotocol.network.protocol.data.HandshakeIntent;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.HandshakePacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.status.ClientStatusRequestPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.status.ServerStatusResponsePacket;

import java.net.Proxy;

public class TestPingBot {
    public static void main(String[] args) {
        MinecraftProtocol.init();

        MinecraftClient client = new MinecraftClient(ConnectionState.STATUS, ProtocolType.PROTOCOL_1_8_X);
        client.setListener(new ClientListener() {
            @Override
            public void connected(Session session) {
                session.sendPacket(new HandshakePacket(client.getProtocol(), session.getServerData().getIp(), session.getServerData().getPort(), HandshakeIntent.STATUS));
                session.sendPacket(new ClientStatusRequestPacket());
            }

            @Override
            public void packetReceived(Session session, Packet packet) {
                if (packet instanceof ServerStatusResponsePacket) {
                    System.out.println(((ServerStatusResponsePacket) packet).getInfo());
                    session.disconnect();
                }
            }

            @Override
            public void disconnected(Session session, String reason, Throwable cause) {
                System.out.println("disconnected, reason: " + reason);
            }
        });

        client.connect("146.59.23.158", 21295, Proxy.NO_PROXY);
    }
}