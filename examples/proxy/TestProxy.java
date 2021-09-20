package examples.proxy;

import examples.proxy.command.CommandManager;
import examples.proxy.handlers.ServerLoginHandler;
import examples.proxy.handlers.ServerStatusHandler;
import examples.proxy.player.PlayerManager;
import me.ANONIMUS.mcprotocol.MinecraftProtocol;
import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.data.HandshakeIntent;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.server.MinecraftServer;
import me.ANONIMUS.mcprotocol.network.server.ServerListener;

public class TestProxy {
    public static void main(String[] args) {
        MinecraftProtocol.init();
        CommandManager.init();

        MinecraftServer server = new MinecraftServer("0.0.0.0", 25565);

        server.setHandler(HandshakeIntent.STATUS, new ServerStatusHandler(server));
        server.setHandler(HandshakeIntent.LOGIN, new ServerLoginHandler(server));

        server.setListener(new ServerListener() {
            @Override
            public void sessionAdded(Session session) {
                PlayerManager.createPlayer(session);
            }

            @Override
            public void sessionRemoved(Session session) {
                PlayerManager.removePlayer(session);
            }

            @Override
            public void packetReceived(Session session, Packet packet) {
//                System.out.println("received: " + packet.getClass().getSimpleName());
            }

            @Override
            public void serverClosed() {
                System.out.println("server closed");
            }
        });

        server.bind();
    }
}