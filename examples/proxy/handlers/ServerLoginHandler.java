package examples.proxy.handlers;

import examples.proxy.util.WorldUtil;
import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.data.ConnectionState;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.login.ClientLoginStartPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.login.ServerLoginSetCompressionPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.login.ServerLoginSuccessPacket;
import me.ANONIMUS.mcprotocol.network.server.MinecraftServer;
import me.ANONIMUS.mcprotocol.network.server.ServerHandler;
import me.ANONIMUS.mcprotocol.objects.GameProfile;

import java.util.UUID;

public class ServerLoginHandler extends ServerHandler {
    public ServerLoginHandler(MinecraftServer server) {
        super(server);
    }

    @Override
    public void disconnect() {
        System.out.println("disconnected");
    }

    @Override
    public void handlePacket(Session session, Packet packet) {
        if (packet instanceof ClientLoginStartPacket) {
            String playerName = ((ClientLoginStartPacket) packet).getUsername();
            GameProfile profile = new GameProfile(UUID.randomUUID(), playerName);
            session.setGameProfile(profile);
            session.sendPacket(new ServerLoginSetCompressionPacket(256));
            session.setCompressionThreshold(256);
            session.sendPacket(new ServerLoginSuccessPacket(profile.getUuid(), playerName));
            session.setConnectionState(ConnectionState.PLAY);
            session.setHandler(new ServerPlayHandler(server, session));

            WorldUtil.emptyWorld(session);
        }
    }
}