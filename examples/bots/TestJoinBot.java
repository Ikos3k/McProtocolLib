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
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.login.ClientLoginStartPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.play.ClientChatPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.login.ServerLoginSuccessPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play.ServerChatPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play.ServerJoinGamePacket;

import java.net.Proxy;

public class TestJoinBot {
    public static void main(String[] args) {
        MinecraftProtocol.init();

        MinecraftClient client = new MinecraftClient(ConnectionState.LOGIN, ProtocolType.PROTOCOL_1_8_X);
        client.setListener(new ClientListener() {
            @Override
            public void connected(Session session) {
                session.sendPacket(new HandshakePacket(client.getProtocol(), session.getServerData().getIp(), session.getServerData().getPort(), HandshakeIntent.LOGIN));
                session.sendPacket(new ClientLoginStartPacket("botTEST"));
            }

            @Override
            public void packetReceived(Session session, Packet packet) {
                if (packet instanceof ServerLoginSuccessPacket) {
                    System.out.println("success packet");
                } else if (packet instanceof ServerJoinGamePacket) {
                    System.out.println("join packet");
                    session.sendPacket(new ClientChatPacket("hi!"));
                } else if (packet instanceof ServerChatPacket) {
                    System.out.println("read message: " + ((ServerChatPacket) packet).getMessage());
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